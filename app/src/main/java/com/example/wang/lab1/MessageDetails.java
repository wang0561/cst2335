package com.example.wang.lab1;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        //Step 3, create fragment onCreation, pass data from Intent Extras to FragmentTransction
        MessageFragment frag = new MessageFragment(null);
        Bundle bun = getIntent().getExtras();
        frag.setArguments( bun );
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder,frag).commit();
    }
}
