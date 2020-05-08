package com.example.firebase4.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.firebase4.R;

import java.util.ArrayList;

public class Fast_Time_Scheduler_Dialog extends DialogFragment implements NumberPicker.OnValueChangeListener{

    private NumberPicker np_start_hour;
    private NumberPicker np_start_minute;
    private TextView tv_am,tv_pm, tv_endHour,tv_endMinute,tv_end_am_pm;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fast_time_scheduler_dialog,null);

        //==================================定義參照=======================================//

        np_start_hour= view.findViewById(R.id.np_start_hour);
        np_start_minute = view.findViewById(R.id.np_start_minute);
        tv_am = view.findViewById(R.id.tv_am);
        tv_pm = view.findViewById(R.id.tv_pm);


        np_start_hour.setOnValueChangedListener(this);

        //=================================獲取activity傳來的資訊=========================================//

        Bundle bundle = getArguments();
        int fastType = bundle.getInt("fastType");
        int weekday = bundle.getInt("weekday");

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


        builder.setView(view);
        return builder.create();
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
}
