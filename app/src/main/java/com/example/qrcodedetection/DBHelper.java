package com.example.qrcodedetection;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "textDb";
    public static final String TABLE_HISTORY = "history";

    public static final String KEY_ID = "_id";
    public static final String KEY_TEXT = "text";


    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + TABLE_HISTORY + "(" + KEY_ID
                + " integer primary key ," + KEY_TEXT + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exist " + TABLE_HISTORY);
        onCreate(database);
    }
}
