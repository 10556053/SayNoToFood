package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirstTimeActivity2 extends AppCompatActivity implements View.OnClickListener {
    private CardView noob,experienced,pro;
    private TextView tv_noob,tv_experienced,tv_pro;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time2);

        noob=(CardView)findViewById(R.id.cd_noob);
        experienced=(CardView)findViewById(R.id.cd_experienced);
        pro=(CardView)findViewById(R.id.cd_pro);
        tv_noob=(TextView)findViewById(R.id.tv_noob);
        tv_experienced=(TextView)findViewById(R.id.tv_experienced);
        tv_pro=(TextView)findViewById(R.id.tv_pro);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId=fAuth.getCurrentUser().getUid();

        noob.setOnClickListener(this);
        experienced.setOnClickListener(this);
        pro.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        String str_noob=tv_noob.getText().toString();
        String str_experienced=tv_experienced.getText().toString();
        String str_pro =tv_pro.getText().toString();
        Map<String,Object> firstTimeData= new HashMap<>();
        DocumentReference documentReference=fStore.collection("users").document(UserId).collection("bodyData").document("my_goal");


        Intent i ;
        switch (v.getId()){
            case R.id.cd_noob:i = new Intent(getApplicationContext(),FirstTimeWeightInput.class);
                firstTimeData.put("experience",str_noob);
                startActivity(i);
                break;

            case R.id.cd_experienced:
                i = new Intent(getApplicationContext(),FirstTimeWeightInput.class);
                firstTimeData.put("experience",str_experienced);
                startActivity(i);
                break;

            case R.id.cd_pro: i = new Intent(getApplicationContext(),FirstTimeWeightInput.class);
                firstTimeData.put("experience",str_pro);
                startActivity(i);
                break;
        }
        documentReference.set(firstTimeData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirstTimeActivity2.this, "Thanks for ur patient", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirstTimeActivity2.this, "Oops, something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
