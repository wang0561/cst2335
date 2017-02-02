package com.example.wang.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.i(ACTIVITY_NAME,"In onCreate");
    }
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume");
    }
    protected void onStart(){
        super.onStart();
        Button lgButton=(Button)findViewById(R.id.loginButton);
        lgButton.setOnClickListener(

                new View.OnClickListener(){
                    public void onClick(View view){
                        SharedPreferences sharedPreferences=getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        EditText loginName=(EditText)findViewById(R.id.editTextLogin);
                        editor.putString("DefaultEmail",loginName.getText().toString());
                        editor.commit();
                        Intent intent=new Intent(LoginActivity.this,StartActivity.class);
                        startActivity(intent);

                    }
                }
        );
        SharedPreferences sharedPreferences=getSharedPreferences("LoginFile",Context.MODE_PRIVATE);
        int numTimeRun=sharedPreferences.getInt("TIMES_RUN",0);
        EditText loginName=(EditText)findViewById(R.id.editTextLogin) ;
        loginName.setText(sharedPreferences.getString("DefaultEmail","email@domain.com"));
        Log.i(ACTIVITY_NAME,"In onStart");
    }
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause");
    }
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy");
    }

}
