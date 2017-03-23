package com.example.wang.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Wang on 2017-02-15.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME="Chats.db";
     public static final int VERSION_NUM=3;
    public static final String KEY_ID="ID";
    public static final String KEY_MESSAGE="MESSAGE";
    public static final String TABLE_NAME="CHAT_TABLE";
    public static final String DATABASE_CREATE= "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +KEY_MESSAGE+" TEXT);";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper","Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper","Calling onUpgrade,oldVersion"+oldVersion+"newVersion="+newVersion);
    }
}
