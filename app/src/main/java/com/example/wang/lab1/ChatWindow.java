package com.example.wang.lab1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
     protected ListView listView;
    protected EditText editText;
    protected Button button;
    protected ArrayList<String> arrayList;
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
        button.setOnClickListener(
                new View.OnClickListener(){

                    public void onClick(View v){
                        if(editText.getText()!=null)
                            arrayList.add(editText.getText().toString());
                        messageAdapter.notifyDataSetChanged();
                        editText.setText("");
                    }
                }

        );


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
