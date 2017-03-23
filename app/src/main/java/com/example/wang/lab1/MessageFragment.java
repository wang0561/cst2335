package com.example.wang.lab1;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    String msg;
    Long msgID;

    ChatWindow chatWindow = null;

    public MessageFragment(){

    }

    public MessageFragment (ChatWindow c){
        chatWindow = c;
    }

    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bundle = getArguments();
        msg = bundle.getString("Message");
        msgID = bundle.getLong("ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View gui = inflater.inflate(R.layout.fragment_layout, null);
        TextView message = (TextView)gui.findViewById(R.id.messageHere);
        message.setText(msg);
        TextView id = (TextView)gui.findViewById(R.id.messageID);
        id.setText("ID:" + msgID);

        Button btnDelete = (Button)gui.findViewById(R.id.deleteMessage);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MessageFragment", "User clicked Delete Message button");
                if (chatWindow == null) {               // called from phone
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DeleteID", msgID);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
                else
                {//from tablet
                    chatWindow.deleteListMessage(msgID);
                    chatWindow.removeFragment();
                }
            }
        });

        return gui;
    }

}