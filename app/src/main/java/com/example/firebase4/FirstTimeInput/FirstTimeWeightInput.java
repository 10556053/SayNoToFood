package com.example.firebase4.FirstTimeInput;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.firebase4.Dialogs.ActiveInputDialog;
import com.example.firebase4.Dialogs.AgeInputDialog;
import com.example.firebase4.Dialogs.BodyFatDialog;
import com.example.firebase4.Dialogs.HeightInputDialog;
import com.example.firebase4.Dialogs.SexInputDialog;
import com.example.firebase4.Dialogs.WaistLengthDialog;
import com.example.firebase4.Dialogs.WeightInputDialog;
import com.example.firebase4.FastEventScheduler.FastEventScheduler;
import com.example.firebase4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstTimeWeightInput extends AppCompatActivity implements View.OnClickListener, SexInputDialog.SexInputDialogListener, WeightInputDialog.WeightInputDialogListener , HeightInputDialog.HeightInputDialogListener, WaistLengthDialog.TargetWeightInputDialogListener ,AgeInputDialog.AgeInputDialogListener,BodyFatDialog.BodyFatInputDialogListener, ActiveInputDialog.ActiveInputDialogListener {
    private CardView cd_gender,cd_age,cd_height,cd_weight,cd_waist_length,cd_body_fat,cd_active;
    private TextView tv_show_gender,tv_age,tv_show_height,tv_show_weight,tv_show_target_weight,tv_show_body_fat,tv_show_active;
    private NumberPicker np;
    private Button bt_next;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private boolean[] check = {false,false,false,false,false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_weight_input);

        cd_gender=(CardView)findViewById(R.id.cd_gender);
        cd_height=(CardView)findViewById(R.id.cd_height);
        cd_weight=(CardView)findViewById(R.id.cd_weight);
        cd_waist_length=(CardView)findViewById(R.id.cd_waist_length);
        cd_age=(CardView)findViewById(R.id.cd_age);
        cd_body_fat=(CardView)findViewById(R.id.cd_body_fat);
        cd_active=(CardView)findViewById(R.id.cd_active);

        tv_age=(TextView) findViewById(R.id.tv_show_age);
        tv_show_gender=(TextView)findViewById(R.id.tv_show_gender);
        tv_show_height=(TextView)findViewById(R.id.tv_show_height);
        tv_show_weight=(TextView)findViewById(R.id.tv_show_weight);
        tv_show_target_weight=(TextView)findViewById(R.id.tv_show_target_weight);
        tv_show_body_fat=(TextView)findViewById(R.id.tv_show_body_fat);
        tv_show_active=(TextView)findViewById(R.id.tv_show_active);

        bt_next = (Button)findViewById(R.id.bt_next);


        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        cd_gender.setOnClickListener(this);
        cd_age.setOnClickListener(this);
        cd_height.setOnClickListener(this);
        cd_weight.setOnClickListener(this);
        cd_waist_length.setOnClickListener(this);
        cd_body_fat.setOnClickListener(this);
        cd_active.setOnClickListener(this);

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()==true){
                    Intent i = new Intent(getApplicationContext(), FastEventScheduler.class);
                    startActivity(i);
                }
            }
        });


    }

    private boolean checkInput() {
        boolean token =true;
        for (int i=0;i<check.length;i++){
            if (check[i]==false){
                animatedAlert(i);
                token = false;
            }
        }
        return  token;

    }

    private void animatedAlert(int i) {
        switch (i){
            case 0:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_gender);
                break;
            case 1:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_age);
                break;
            case 2:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_height);
                break;
            case 3:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_weight);
                break;
            case 4:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_waist_length);
                break;
            case 5:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_body_fat);
                break;
            case 6:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_active);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cd_gender:
                showSex();
                break;

            case R.id.cd_height:
                showHeight();
                break;

            case R.id.cd_weight:
                showWeight();
                break;
            case R.id.cd_waist_length:
                show_Target_Weight();
                break;
            case R.id.cd_age:
                showAge();
                break;
            case R.id.cd_body_fat:
                showBodyFat();
                break;
            case R.id.cd_active:
                showActive();
                break;
        }
    }

    private void showActive() {
        ActiveInputDialog activeInputDialog = new ActiveInputDialog();
        activeInputDialog.show(getSupportFragmentManager(),"active_dialog");
    }

    private void showBodyFat() {
        BodyFatDialog bodyFatDialog = new BodyFatDialog();
        bodyFatDialog.show(getSupportFragmentManager(),"fat_input_dialog");
    }

    private void showAge() {
        AgeInputDialog ageInputDialog = new AgeInputDialog();
        ageInputDialog.show(getSupportFragmentManager(),"age_input_dialog");
    }

    private void showSex() {
        SexInputDialog sexInputDialog = new SexInputDialog();
        sexInputDialog.show(getSupportFragmentManager(),"sex_input_dialog");

    }

    private void showHeight() {
        HeightInputDialog heightInputDialog = new HeightInputDialog();
        heightInputDialog.show(getSupportFragmentManager(),"height_input_dialog");

    }

    private void show_Target_Weight() {
        WaistLengthDialog waistLengthDialog = new WaistLengthDialog();
        waistLengthDialog.show(getSupportFragmentManager(),"target_weight_input_dialog");

    }


    private void showWeight() {
        WeightInputDialog weightInputDialog = new WeightInputDialog();
        weightInputDialog.show(getSupportFragmentManager(),"weight_input_dialog");

    }


    @Override
    public void applyWeight(String weight) {
        tv_show_weight.setText(weight+"公斤");
        check[3]=true;
    }

    @Override
    public void applyHeight(String height) {
        tv_show_height.setText(height+"公分");
        check[2]=true;
    }

    @Override
    public void applyTargetWeight(String target_weight) {
        tv_show_target_weight.setText(target_weight+"公分");
        check[4]=true;
    }

    @Override
    public void applySex(String sex) {
        tv_show_gender.setText(sex);
        check[0]=true;
    }

    @Override
    public void applyAge(String age) {
        tv_age.setText(age+"歲");
        check[1]=true;
    }

    @Override
    public void applyBodyFat(String fat) {
        tv_show_body_fat.setText(fat+"%");
        check[5]=true;
    }

    @Override
    public void applyActive(String active) {
        tv_show_active.setText("每日活動:"+active);
        check[6]=true;
    }
}
