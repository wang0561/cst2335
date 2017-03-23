package com.example.wang.lab1;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected  static final String ACTIVITY_NAME="ChatWindow";
     protected ListView listView;
    protected EditText editText;
    protected Button button;
    protected ArrayList<String> arrayList;
    protected ChatDatabaseHelper myHelper;
    protected ChatAdapter messageAdapter;
    protected Cursor cursor;
    protected boolean isLoad;
    protected  MessageFragment frag;
   protected   SQLiteDatabase db;
    protected ArrayList<Long> idresult=new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        isLoad= (findViewById(R.id.fragmentHolder)!=null);

        listView =(ListView)findViewById(R.id.listView);
        editText=(EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.sendB);
        arrayList=new ArrayList<>();
         messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        myHelper=new ChatDatabaseHelper(this);

        db=myHelper.getWritableDatabase();
        cursor=db.query(false,myHelper.TABLE_NAME,new String[]{myHelper.KEY_ID,
                        myHelper.KEY_MESSAGE},
                myHelper.KEY_ID+" NOT NULL",null,null
                ,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            idresult.add(cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID)));
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
                       idresult.add( db.insert(ChatDatabaseHelper.TABLE_NAME,"",contentValues));
                        editText.setText("");

                    }
                }

        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("chatListView", "onItemClick: " + i + " " + l);

                Bundle bun = new Bundle();
                bun.putLong("ID", l);//l is the database ID of selected item
                String msg = messageAdapter.getItem(i);
                bun.putString("Message", msg);

                //  tablet
                if(isLoad) {
                    frag = new MessageFragment(ChatWindow.this);

                    frag.setArguments(bun);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, frag).commit();
                }

                else //Phone
                {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("ID" , l);
                    intent.putExtra("Message", msg);//pass the Database ID and message to next activity
                    startActivityForResult(intent
                            , 5); //go to view fragment details
                }
            }
        });
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
        public long getItemId(int position){
           // cursor.moveToPosition(position);
            return idresult.get(position);
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
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if ((requestCode == 5) && (resultCode == Activity.RESULT_OK)) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");

            Long deleteId = data.getLongExtra("DeleteID", -1);
            arrayList.remove(deleteId);
            deleteListMessage(deleteId);

             messageAdapter.notifyDataSetChanged();
        }
    }
    public void deleteListMessage(Long id)
    {

       final  SQLiteDatabase db=myHelper.getWritableDatabase();
        db.delete(ChatDatabaseHelper.TABLE_NAME, "ID =" + id , null);
        idresult.clear();
        arrayList.clear();
        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},null, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            idresult.add(cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            cursor.moveToNext();

        }

        messageAdapter.notifyDataSetChanged();
    }

    public void removeFragment()
    {
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }
}
