package com.example.firebase4.WeekView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowWeekView extends AppCompatActivity implements MonthLoader.MonthChangeListener, WeekView.EmptyViewClickListener,WeekView.EventClickListener,WeekView.EventLongPressListener{
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    private WeekView mWeekView;
    private ArrayList<WeekViewEvent> mNewEvents;

    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;
    public static final String DATABASE_NAME = "Event_Db";
    public static final String TABLE_NOVICE="table_novice";
    public static final String TABLE_ADVANCED="table_advanced";
    public static final String TABLE_HARD="table_hard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_week_view);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set up empty view click listener.
        mWeekView.setEmptyViewClickListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setNumberOfVisibleDays(5);
        mWeekView.setXScrollingSpeed(0);


        // Initially, there will be no events on the week view because the user has not tapped on
        // it yet.
        mNewEvents = new ArrayList<WeekViewEvent>();



        readEvent();
    }

    public void readEvent(){
        Bundle bundle = this.getIntent().getExtras();
        int schedule_type = bundle.getInt("schedule_type");

        switch (schedule_type){
            case 1:
                Toast.makeText(ShowWeekView.this, "schedule type novice", Toast.LENGTH_SHORT).show();
                exeSql_Novice();
                break;
            case 2:
                Toast.makeText(ShowWeekView.this, "schedule type advanced", Toast.LENGTH_SHORT).show();
                exeSql_Advanced();
                break;
            case 3:
                Toast.makeText(ShowWeekView.this, "schedule type hard", Toast.LENGTH_SHORT).show();
                exeSql_Hard();
                break;
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with the events that was added by tapping on empty view.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);
        events.addAll(newEvents);
        return events;
    }

    /**
     * Get events that were added by tapping on empty view.
     * @param year The year currently visible on the week view.
     * @param month The month currently visible on the week view.
     * @return The events of the given year and month.
     */
    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        // Set the new event with duration one hour.
        //Calendar endTime = (Calendar) time.clone();
        //endTime.add(Calendar.HOUR, 1);

        // Create a new event.
        //WeekViewEvent event = new WeekViewEvent(20, "New event", time, endTime);
        //mNewEvents.add(event);

        // Refresh the week view. onMonthChange will be called again.
        //mWeekView.notifyDatasetChanged();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }


//==================================================================================================//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_5_day:
                mWeekView.setNumberOfVisibleDays(5);
                mWeekView.setXScrollingSpeed(0);
                break;
            case R.id.action_7_day:
                mWeekView.setNumberOfVisibleDays(7);
                mWeekView.setXScrollingSpeed(0);
                break;
            case R.id.action_calendar_view:
                mWeekView.setNumberOfVisibleDays(7);
                mWeekView.setXScrollingSpeed(1);
                break;

            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_next:
                //Intent i = new Intent(ShowWeekView.this, HomePageActivity.class);
                //startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    public void exeSql_Novice(){
        Toast.makeText(ShowWeekView.this, "login successful", Toast.LENGTH_SHORT).show();

        String start,end;
        int dur,id;
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(ShowWeekView.this);
        db = sqLiteDataBaseHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_NOVICE, null);    // 查詢tb_name資料表中的所有資料

        if (c.getCount()>0){    // 若有資料

            c.moveToFirst();    // 移到第 1 筆資料
            do{        // 逐筆讀出資料
                id = c.getInt(0);
                start = c.getString(1);
                dur = c.getInt(2);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar startTime;
                startTime = Calendar.getInstance();

                try {
                    startTime.setTime(df.parse(start));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar endTime = (Calendar) startTime.clone() ;
                endTime.add(Calendar.HOUR,dur);
                WeekViewEvent event = new WeekViewEvent(id,"newEvent"+id,startTime,endTime);//問題出在start time，三者的startTime會變成一模一樣，都已最後一次讀出的startTime為主
                mNewEvents.add(event);
                mWeekView.notifyDatasetChanged();

            } while(c.moveToNext());    // 有一下筆就繼續迴圈


        }

    }

    public void exeSql_Advanced(){
        Toast.makeText(ShowWeekView.this, "login successful", Toast.LENGTH_SHORT).show();

        String start,end;
        int dur,id;
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(ShowWeekView.this);
        db = sqLiteDataBaseHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_ADVANCED, null);    // 查詢tb_name資料表中的所有資料

        if (c.getCount()>0){    // 若有資料

            c.moveToFirst();    // 移到第 1 筆資料
            do{        // 逐筆讀出資料
                id = c.getInt(0);
                start = c.getString(1);
                dur = c.getInt(2);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar startTime;
                startTime = Calendar.getInstance();

                try {
                    startTime.setTime(df.parse(start));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar endTime = (Calendar) startTime.clone() ;
                endTime.add(Calendar.HOUR,dur);
                WeekViewEvent event = new WeekViewEvent(id,"newEvent"+id,startTime,endTime);//問題出在start time，三者的startTime會變成一模一樣，都已最後一次讀出的startTime為主
                mNewEvents.add(event);
                mWeekView.notifyDatasetChanged();

            } while(c.moveToNext());    // 有一下筆就繼續迴圈


        }

    }
    public void exeSql_Hard(){
        Toast.makeText(ShowWeekView.this, "login successful", Toast.LENGTH_SHORT).show();

        String start,end;
        int dur,id;
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(ShowWeekView.this);
        db = sqLiteDataBaseHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_HARD, null);    // 查詢tb_name資料表中的所有資料

        if (c.getCount()>0){    // 若有資料

            c.moveToFirst();    // 移到第 1 筆資料
            do{        // 逐筆讀出資料
                id = c.getInt(0);
                start = c.getString(1);
                dur = c.getInt(2);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar startTime;
                startTime = Calendar.getInstance();

                try {
                    startTime.setTime(df.parse(start));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar endTime = (Calendar) startTime.clone() ;
                endTime.add(Calendar.HOUR,dur);
                WeekViewEvent event = new WeekViewEvent(id,"newEvent"+id,startTime,endTime);//問題出在start time，三者的startTime會變成一模一樣，都已最後一次讀出的startTime為主
                mNewEvents.add(event);
                mWeekView.notifyDatasetChanged();

            } while(c.moveToNext());    // 有一下筆就繼續迴圈


        }

    }

}
