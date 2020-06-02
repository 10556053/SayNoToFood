package com.example.firebase4.FastEventScheduler;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.Dialogs.Custom_AlertDialog;
import com.example.firebase4.FirstTimeInput.FirstTimeWeightInput;
import com.example.firebase4.R;
import com.example.firebase4.WeekView.ShowWeekView;
import com.facebook.stetho.Stetho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FastEventScheduler extends AppCompatActivity implements View.OnClickListener{
    Dialog alert_Dialog;
    private CardView cd_novice,cd_experienced,cd_advanced,cd_hardcore,cd_perfect,cd_incredible;

    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_event_scheduler);

        cd_novice = (CardView)findViewById(R.id.cd_novice);
        cd_advanced = (CardView)findViewById(R.id.cd_advanced);
        cd_experienced = (CardView)findViewById(R.id.cd_experienced);
        cd_hardcore = (CardView)findViewById(R.id.cd_hardcore);


        cd_novice.setOnClickListener(this);
        cd_experienced.setOnClickListener(this);
        cd_advanced.setOnClickListener(this);
        cd_hardcore.setOnClickListener(this);

        //Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onClick(View v) {



        switch (v.getId()){

            case R.id.cd_novice:
                reset_Data();
                putDataUserWeekPlan( 10 );
                openAlertDialog(0);

        }
        switch (v.getId()){
            case R.id.cd_experienced:
                reset_Data();
                putDataUserWeekPlan( 8 );
                openAlertDialog(1);
                //putDataAdvanced();

        }
        switch (v.getId()){
            case R.id.cd_advanced:
                reset_Data();
                putDataUserWeekPlan( 6 );
                openAlertDialog(2);
                //putDataHard();

        }
        switch (v.getId()){
            case R.id.cd_hardcore:
                reset_Data();
                putDataUserWeekPlan( 6 );
                openAlertDialog(3);
                //putDataHard();

        }

    }

    private void reset_Data() {
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(FastEventScheduler.this);
        db=sqLiteDataBaseHelper.getWritableDatabase();
        db.execSQL("delete from user_week_plan" );
        db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'user_week_plan'");
        db.execSQL("delete from user_fast_history" );
        db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'user_fast_history'");

        Toast.makeText(FastEventScheduler.this , "資料已重置",  Toast.LENGTH_SHORT).show();
    }

    private void openAlertDialog(int fastType) {

        Custom_AlertDialog alertDialog = new Custom_AlertDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("fastType",fastType);

        alertDialog.setArguments(bundle);
        alertDialog.show(getSupportFragmentManager(),"date_input_dialog");

    }

    /*public void putData(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        ArrayList<Integer> scheduler = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            scheduler.add( i * 7 + 0 );
            scheduler.add( i * 7 + 1 );
            scheduler.add( i * 7 + 5 );
            scheduler.add( i * 7 + 6 );
        }

        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(FastEventScheduler.this);

        for (int i = 0;i<scheduler.size();i++){
            Calendar startTime = Calendar.getInstance();
            int duration = 8;

            SimpleDateFormat df = new  SimpleDateFormat("yyyy-MM-dd HH:mm ,EEEE");
            startTime.set(Calendar.YEAR,year);
            startTime.set(Calendar.MONTH,month);
            startTime.set(Calendar.DAY_OF_MONTH,(day+scheduler.get(i)));
            startTime.set(Calendar.HOUR_OF_DAY,12);
            startTime.set(Calendar.MINUTE,0);
            startTime.set(Calendar.SECOND,0);

            String st_startTime = df.format(startTime.getTime());
            //sqLiteDataBaseHelper.putInNoviceTable(st_startTime,duration);
        }

        Intent i = new Intent(getApplicationContext(), ShowWeekView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("schedule_type",1);
        i.putExtras(bundle);
        startActivity(i);
    }*/

    public void putDataUserWeekPlan(int duration){
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(FastEventScheduler.this);

        for (int i = 1;i<8;i++){
            Calendar startTime = Calendar.getInstance();


            SimpleDateFormat df = new  SimpleDateFormat("HH:mm:ss");

            startTime.set(Calendar.HOUR_OF_DAY,12);
            startTime.set(Calendar.MINUTE,30);
            startTime.set(Calendar.SECOND,0);

            String st_startTime = df.format(startTime.getTime());
            sqLiteDataBaseHelper.putInWeekPlanTable(i,st_startTime,duration);
        }


    }




}
