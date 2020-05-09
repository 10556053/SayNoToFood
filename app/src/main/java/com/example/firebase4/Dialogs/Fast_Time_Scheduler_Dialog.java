package com.example.firebase4.Dialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.FastEventScheduler.FastEventScheduler;
import com.example.firebase4.R;
import com.facebook.stetho.Stetho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Fast_Time_Scheduler_Dialog extends DialogFragment implements NumberPicker.OnValueChangeListener{
    public static final String USER_WEEK_PLAN="user_week_plan";
    private NumberPicker np_start_hour;
    private NumberPicker np_start_minute;
    private TextView tv_am,tv_pm, tv_endHour,tv_endMinute,tv_end_am_pm;
    private FasTimeInputDialogListener fasTimeInputDialogListener;
    private Button bt_time_select_done;
    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fast_time_scheduler_dialog,null);
         final Context context = builder.getContext();

        //==================================定義參照=======================================//

        np_start_hour= view.findViewById(R.id.np_start_hour);
        np_start_minute = view.findViewById(R.id.np_start_minute);
        tv_am = view.findViewById(R.id.tv_am);
        tv_pm = view.findViewById(R.id.tv_pm);
        bt_time_select_done = view.findViewById(R.id.bt_time_select_done);


        np_start_hour.setOnValueChangedListener(this);

        //=================================獲取activity傳來的資訊=========================================//

        Bundle bundle = getArguments();
        final int fastType = bundle.getInt("fastType");
        final int weekday = bundle.getInt("weekday");

        //==============================設定NumberPicker====================================//

        switch (fastType){

            //16/8
            //設定12小時制?
            case 0:
                np_start_hour.setMinValue(7);
                np_start_hour.setMaxValue(16);
                np_start_hour.setValue(12);
                break;
             //15/9
            case 1:
                np_start_hour.setMinValue(6);
                np_start_hour.setMaxValue(14);
                np_start_hour.setValue(12);
                break;
            //14/10
            case 2:
                np_start_hour.setMinValue(5);
                np_start_hour.setMaxValue(13);
                np_start_hour.setValue(12);
                break;

            //19/5
            case 3:
                np_start_hour.setMinValue(8);
                np_start_hour.setMaxValue(18);
                np_start_hour.setValue(12);
                break;

            //20/4
            case 4:
                np_start_hour.setMinValue(8);
                np_start_hour.setMaxValue(19);
                np_start_hour.setValue(12);
                break;
            //22/2
            case 5:
                np_start_hour.setMinValue(8);
                np_start_hour.setMaxValue(20);
                np_start_hour.setValue(12);
                break;

        }


        np_start_minute.setMinValue(0);
        np_start_minute.setMaxValue(59);
        np_start_minute.setValue(30);

        int size = np_start_hour.getMaxValue() - np_start_hour.getMinValue() + 1;
        String[] numberList = new String[size];

        int addr = 0;
        for ( int i =  np_start_hour.getMinValue(); i <= np_start_hour.getMaxValue(); i++ ) {
            numberList[addr++] = i % 12 == 0 ? "12" : String.valueOf( i % 12 );
        }

        np_start_hour.setDisplayedValues( numberList );

//====================bt done clicked================================================
        bt_time_select_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = np_start_hour.getValue();
                int minute = np_start_minute.getValue();
                int fastHour = 0;
                switch (fastType){

                    case 0:
                        fastHour = 8;
                        break;
                    case 1:
                        fastHour = 9;
                        break;
                    case 2:
                        fastHour = 10;
                        break;
                    case 3:
                        fastHour = 5;
                        break;
                    case 4:
                        fastHour = 4;
                        break;
                    case 5:
                        fastHour = 2;
                        break;
                }
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,hour);
                c.set(Calendar.MINUTE,minute);
                c.set(Calendar.SECOND,0);

                String st_starTime = df.format(c.getTime());

                updateData(context,weekday,st_starTime,fastHour);
                fasTimeInputDialogListener.apply_time(weekday,fastHour,hour,minute);
                dismiss();

            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void updateData(Context c ,int weekday,String startime, int duration) {
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(c);
        db=sqLiteDataBaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("STARTIME",startime);


        db.update(USER_WEEK_PLAN, contentValues, "WEEKDAY = " + weekday, null);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fasTimeInputDialogListener = (FasTimeInputDialogListener)context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement WeightInputDialogListener");
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        if (newVal>=12){
            tv_am.setTextSize(16);
            tv_pm.setTextSize(22);
        }else{
            tv_pm.setTextSize(16);
            tv_am.setTextSize(22);
        }
    }

    public interface FasTimeInputDialogListener{
        void apply_time(int weekday,int fastHour,int hour,int minute);
    }


}
