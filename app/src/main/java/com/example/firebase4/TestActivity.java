package com.example.firebase4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.FastEventScheduler.FastEventScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestActivity extends AppCompatActivity {

    private TextView tv_showFetchedTime;
    public static final String USER_WEEK_PLAN="user_week_plan";
    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv_showFetchedTime = findViewById(R.id.tv_showFetchedTime);

        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(TestActivity.this);
        db=sqLiteDataBaseHelper.getReadableDatabase();


        //取得當前星期幾
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int i =calendar.get(Calendar.DAY_OF_WEEK); // the day of the week in numerical format

        Cursor c = db.rawQuery(" SELECT * FROM " + USER_WEEK_PLAN
                + " WHERE WEEKDAY =" + "'" + i + "'", null);

        while (c.moveToNext()) {


            int id = c.getInt(0);
            int weekday = c.getInt(1);
            String startime = c.getString(2);
            int duration = c.getInt(3);
            Calendar start = Calendar.getInstance();
            SimpleDateFormat df = new  SimpleDateFormat("HH:mm:ss");
            try {
                start.setTime(df.parse(startime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c1 = (Calendar) start.clone();
            c1.add(Calendar.HOUR_OF_DAY,duration);
            String endtime = df.format(c1.getTime());
            String s = String.format("開始時間為 %s,結束時間為%s ",startime,endtime);
            tv_showFetchedTime.setText(s);

        }





    }
}
