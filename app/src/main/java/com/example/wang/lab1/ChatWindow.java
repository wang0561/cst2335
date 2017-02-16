package com.example.wang.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected  static final String ACTIVITY_NAME="ChatWindow";
     protected ListView listView;
    protected EditText editText;
    protected Button button;
    protected ArrayList<String> arrayList;
    protected ChatDatabaseHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView =(ListView)findViewById(R.id.listView);
        editText=(EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.sendB);
        arrayList=new ArrayList<>();
        final ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        myHelper=new ChatDatabaseHelper(this);

        final SQLiteDatabase db=myHelper.getWritableDatabase();
        Cursor cursor=db.query(false,myHelper.TABLE_NAME,new String[]{myHelper.KEY_ID,
                        myHelper.KEY_MESSAGE},
                myHelper.KEY_ID+" NOT NULL",null,null
                ,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        button.setOnClickListener(
                new View.OnClickListener(){

                    public void onClick(View v){

                            arrayList.add(editText.getText().toString());
                        messageAdapter.notifyDataSetChanged();
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(ChatDatabaseHelper.KEY_MESSAGE,editText.getText().toString());
                        db.insert(ChatDatabaseHelper.TABLE_NAME,"",contentValues);
                        editText.setText("");

                    }
                }

        );
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
        for(int i=0;i<cursor.getColumnCount();i++){
            System.out.println(cursor.getColumnName(i));
        }


    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        myHelper.close();
        Log.i(ACTIVITY_NAME,"In onDestroy");
    }
    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
            return arrayList.size();
        }
        public String getItem(int position){
            return arrayList.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;



        }

    }

}
