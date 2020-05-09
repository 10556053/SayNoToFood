package com.example.firebase4.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Event_Db";
    public static final String USER_WEEK_PLAN="user_week_plan";
    public static final String USER_FAST_HISTORY="user_fast_history";


    public SQLiteDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //創建資料表
         String table_user_week_plan = "CREATE TABLE IF NOT EXISTS " + USER_WEEK_PLAN + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "WEEKDAY INTEGER ," +
                 "STARTIME VARCHAR(50) ," +
                "DURATION INTEGER " +
                ");";
        String table_user_fast_history = "CREATE TABLE IF NOT EXISTS " + USER_FAST_HISTORY + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "STARTIME VARCHAR(50) ," +
                "DURATION INTEGER " +
                ");";


        db.execSQL(table_user_week_plan);
        db.execSQL(table_user_fast_history);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //drop existing table
        db.execSQL("DROP TABLE IF EXISTS "+USER_WEEK_PLAN);
        db.execSQL("DROP TABLE IF EXISTS "+USER_FAST_HISTORY);

        onCreate(db);

    }

    //create insert method


    //create PUT startTime method
    public void putInWeekPlanTable( int weekday, String startTime , int duration){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("WEEKDAY",weekday);
        values1.put("STARTIME",startTime);
        values1.put("DURATION",duration);
        sqLiteDatabase.insert(USER_WEEK_PLAN,null,values1);


        //Perform rawQuery

    }

    public void putUserFastHistory(String startTime , int duration){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("STARTIME",startTime);
        values1.put("DURATION",duration);
        sqLiteDatabase.insert(USER_FAST_HISTORY,null,values1);

        //Perform rawQuery

    }




}
