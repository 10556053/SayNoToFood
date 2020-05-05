package com.example.firebase4.FirstTimeInput;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase4.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirstTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView lose_weight,slimmer,healthier;
    private TextView tv_weight,tv_slim,tv_health;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        lose_weight=(CardView)findViewById(R.id.cd_lose_weight);
        slimmer=(CardView)findViewById(R.id.cd_slimmer);
        healthier=(CardView)findViewById(R.id.cd_healthier);
        tv_weight=(TextView)findViewById(R.id.tv_weight);
        tv_slim=(TextView)findViewById(R.id.tv_slim);
        tv_health=(TextView)findViewById(R.id.tv_health);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId=fAuth.getCurrentUser().getUid();

        lose_weight.setOnClickListener(this);
        slimmer.setOnClickListener(this);
        healthier.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String weight=tv_weight.getText().toString();
        String slim=tv_slim.getText().toString();
        String health=tv_health.getText().toString();
        Map<String,Object> firstTimeData= new HashMap<>();
        DocumentReference documentReference=fStore.collection("users").document(UserId).collection("bodyData").document("my_goal");

        Intent i ;
        switch (v.getId()){
            case R.id.cd_lose_weight:
                //i = new Intent(getApplicationContext(),FirstTimeActivity2.class);
                //firstTimeData.put("goal",weight);
                //startActivity(i);
            break;

            case R.id.cd_slimmer:
                //i = new Intent(getApplicationContext(),FirstTimeActivity2.class);
                //firstTimeData.put("goal",slim);
                //startActivity(i);
            break;

            //case R.id.cd_healthier: i = new Intent(getApplicationContext(),FirstTimeActivity2.class);
                //firstTimeData.put("goal",health);
                //startActivity(i);
            //break;
        }
        documentReference.set(firstTimeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirstTimeActivity.this, "Thanks for ur patient", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirstTimeActivity.this, "Oops, something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
