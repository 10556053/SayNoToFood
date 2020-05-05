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
    public static final String TABLE_NOVICE="table_novice";
    public static final String TABLE_ADVANCED="table_advanced";
    public static final String TABLE_HARD="table_hard";

    public SQLiteDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //創建資料表
         String table_novice = "CREATE TABLE IF NOT EXISTS " + TABLE_NOVICE + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "STARTIME VARCHAR(50) ," +
                "DURATION INTEGER " +
                ");";
        String table_advanced = "CREATE TABLE IF NOT EXISTS " + TABLE_ADVANCED + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "STARTIME VARCHAR(50) ," +
                "DURATION INTEGER " +
                ");";
        String table_hard = "CREATE TABLE IF NOT EXISTS " + TABLE_HARD + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "STARTIME VARCHAR(50) ," +
                "DURATION INTEGER " +
                ");";

        db.execSQL(table_novice);
        db.execSQL(table_advanced);
        db.execSQL(table_hard);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //drop existing table
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOVICE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ADVANCED);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HARD);
        onCreate(db);

    }

    //create insert method


    //create PUT startTime method
    public void putInNoviceTable(String startTime , int duration){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("STARTIME",startTime);
        values1.put("DURATION",duration);
        sqLiteDatabase.insert(TABLE_NOVICE,null,values1);


        //Perform rawQuery

    }

    public void putInAdvancedTable(String startTime , int duration){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("STARTIME",startTime);
        values1.put("DURATION",duration);
        sqLiteDatabase.insert(TABLE_ADVANCED,null,values1);

        //Perform rawQuery

    }

    public void putInHardTable(String startTime , int duration){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("STARTIME",startTime);
        values1.put("DURATION",duration);
        sqLiteDatabase.insert(TABLE_HARD,null,values1);

        //Perform rawQuery

    }


}
