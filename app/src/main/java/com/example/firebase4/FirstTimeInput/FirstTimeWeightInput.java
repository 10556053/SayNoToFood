package com.example.firebase4.FirstTimeInput;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.Dialogs.HeightInputDialog;
import com.example.firebase4.Dialogs.SexInputDialog;
import com.example.firebase4.Dialogs.TargetWeightInputDialog;
import com.example.firebase4.Dialogs.WeightInputDialog;
import com.example.firebase4.FastEventScheduler.FastEventScheduler;
import com.example.firebase4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Dictionary;
import java.util.Hashtable;

public class FirstTimeWeightInput extends AppCompatActivity implements View.OnClickListener, SexInputDialog.SexInputDialogListener, WeightInputDialog.WeightInputDialogListener , HeightInputDialog.HeightInputDialogListener, TargetWeightInputDialog.TargetWeightInputDialogListener {
    private CardView cd_gender,cd_height,cd_weight,cd_target_weight;
    private TextView tv_show_gender,tv_show_height,tv_show_weight,tv_show_target_weight;
    private NumberPicker np;
    private Button bt_next;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private boolean[] check = {false,false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_weight_input);

        cd_gender=(CardView)findViewById(R.id.cd_gender);
        cd_height=(CardView)findViewById(R.id.cd_height);
        cd_weight=(CardView)findViewById(R.id.cd_weight);
        cd_target_weight=(CardView)findViewById(R.id.cd_target_weight);

        tv_show_gender=(TextView)findViewById(R.id.tv_show_gender);
        tv_show_height=(TextView)findViewById(R.id.tv_show_height);
        tv_show_weight=(TextView)findViewById(R.id.tv_show_weight);
        tv_show_target_weight=(TextView)findViewById(R.id.tv_show_target_weight);

        bt_next = (Button)findViewById(R.id.bt_next);


        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        cd_gender.setOnClickListener(this);
        cd_height.setOnClickListener(this);
        cd_weight.setOnClickListener(this);
        cd_target_weight.setOnClickListener(this);

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
                        .playOn(cd_height);
                break;
            case 2:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_weight);
                break;
            case 3:
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(cd_target_weight);
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
            case R.id.cd_target_weight:
                show_Target_Weight();
                break;
        }
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
        TargetWeightInputDialog targetWeightInputDialog = new TargetWeightInputDialog();
        targetWeightInputDialog.show(getSupportFragmentManager(),"target_weight_input_dialog");

    }


    private void showWeight() {
        WeightInputDialog weightInputDialog = new WeightInputDialog();
        weightInputDialog.show(getSupportFragmentManager(),"weight_input_dialog");

    }


    @Override
    public void applyWeight(String weight) {
        tv_show_weight.setText(weight+"公斤");
        check[2]=true;
    }

    @Override
    public void applyHeight(String height) {
        tv_show_height.setText(height+"公分");
        check[1]=true;
    }

    @Override
    public void applyTargetWeight(String target_weight) {
        tv_show_target_weight.setText(target_weight+"公斤");
        check[3]=true;
    }

    @Override
    public void applySex(String sex) {
        tv_show_gender.setText(sex);
        check[0]=true;
    }
}
