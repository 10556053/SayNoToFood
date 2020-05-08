package com.example.firebase4.FastEventScheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.firebase4.Dialogs.Custom_AlertDialog;
import com.example.firebase4.Dialogs.Fast_Time_Scheduler_Dialog;
import com.example.firebase4.R;

public class EventTimePicker extends AppCompatActivity implements View.OnClickListener{
    private CardView cd_Mon,cd_Tue,cd_Wed,cd_Thu,cd_Fri,cd_Sat,cd_Sun;
    private Button bt_finished;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_time_picker);
        cd_Mon = findViewById(R.id.cd_Mon);
        cd_Tue = findViewById(R.id.cd_Tue);
        cd_Wed = findViewById(R.id.cd_Wed);
        cd_Thu = findViewById(R.id.cd_Thu);
        cd_Fri = findViewById(R.id.cd_Fri);
        cd_Sat = findViewById(R.id.cd_Sat);
        cd_Sun = findViewById(R.id.cd_Sun);

        bt_finished = findViewById(R.id.bt_finished);

        cd_Mon.setOnClickListener(this);
        cd_Tue.setOnClickListener(this);
        cd_Wed.setOnClickListener(this);
        cd_Thu.setOnClickListener(this);
        cd_Fri.setOnClickListener(this);
        cd_Sat.setOnClickListener(this);
        cd_Sun.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        Bundle bundle =i.getExtras();
        int fastType = bundle.getInt("fastType");
        switch (v.getId()){

            case R.id.cd_Mon:
                openFastingPeriodDialog( fastType,2);


        }
        switch (v.getId()){
            case R.id.cd_Tue:
                openFastingPeriodDialog(fastType,3);


        }
        switch (v.getId()){
            case R.id.cd_Wed:
                openFastingPeriodDialog(fastType,4);


        }
        switch (v.getId()){
            case R.id.cd_Thu:
                openFastingPeriodDialog(fastType,5);


        }
        switch (v.getId()){
            case R.id.cd_Fri:
                openFastingPeriodDialog(fastType,6);


        }
        switch (v.getId()){
            case R.id.cd_Sat:
                openFastingPeriodDialog(fastType,7);


        }
        switch (v.getId()){
            case R.id.cd_Sun:
                openFastingPeriodDialog(fastType,1);


        }

    }

    private void openFastingPeriodDialog(int fastType,int num_of_weekday) {
        Fast_Time_Scheduler_Dialog fast_time_dialog = new Fast_Time_Scheduler_Dialog();
        Bundle bundle = new Bundle();

        bundle.putInt("fastType",fastType);
        bundle.putInt("weekday",num_of_weekday);


        fast_time_dialog.setArguments(bundle);
        fast_time_dialog.show(getSupportFragmentManager(),"date_input_dialog");
    }
}
