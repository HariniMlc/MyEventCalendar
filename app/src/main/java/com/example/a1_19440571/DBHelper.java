package com.example.a1_19440571;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    //--Create database
    public DBHelper(Context context) {
        super(context, "MyCalendar.db", null, 1);
    }

    //--Create table to input events
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table event_tab(id TEXT primary key, name TEXT, description TEXT, location TEXT,event_date TEXT, start_time TEXT, end_time TEXT)");
    }
    //--Drop and create table when updating app
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists event_tab");
    }
    //--Insert event values
    public Boolean insertuserdata(String name, String location, String description, String event_date, String start_time, String end_time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id = name+"^"+event_date+"^"+start_time+"^"+end_time;
        contentValues.put("id", id);
        contentValues.put("event_date", event_date);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("location", location);
        long result=DB.insert("event_tab", null, contentValues);

        //--return whether the entry was a success or not
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

}
