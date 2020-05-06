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

import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.InputDialog.HeightInputDialog;
import com.example.firebase4.InputDialog.SexInputDialog;
import com.example.firebase4.InputDialog.TargetWeightInputDialog;
import com.example.firebase4.InputDialog.WeightInputDialog;
import com.example.firebase4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstTimeWeightInput extends AppCompatActivity implements View.OnClickListener, SexInputDialog.SexInputDialogListener, WeightInputDialog.WeightInputDialogListener , HeightInputDialog.HeightInputDialogListener, TargetWeightInputDialog.TargetWeightInputDialogListener {
    private CardView cd_gender,cd_height,cd_weight,cd_target_weight;
    private TextView tv_show_gender,tv_show_height,tv_show_weight,tv_show_target_weight;
    private NumberPicker np;
    private Button bt_next;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserId;
    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;
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
                sqLiteDataBaseHelper = new SQLiteDataBaseHelper(FirstTimeWeightInput.this);
                db=sqLiteDataBaseHelper.getWritableDatabase();
                db.execSQL("delete from table_novice" );
                db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'table_novice'");
                db.execSQL("delete from table_advanced" );
                db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'table_advanced'");
                db.execSQL("delete from table_hard" );
                db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'table_hard'");
                Toast.makeText(FirstTimeWeightInput.this , "已刪除",  Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), FastEventScheduler.class);
                startActivity(i);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.cd_gender:
                //show();
                showSex();
                //startActivity(i);
                break;

            case R.id.cd_height:
                //i = new Intent(getApplicationContext(),FirstTimeActivity2.class);
                //startActivity(i);
                showHeight();
                break;

            case R.id.cd_weight:
                showWeight();
                //startActivity(i);
                break;
            case R.id.cd_target_weight:
                show_Target_Weight();
                //startActivity(i);
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
    }

    @Override
    public void applyHeight(String height) {
        tv_show_height.setText(height+"公分");
    }

    @Override
    public void applyTargetWeight(String target_weight) {
        tv_show_target_weight.setText(target_weight+"公斤");
    }

    @Override
    public void applySex(String sex) {
        tv_show_gender.setText(sex);
    }
}
