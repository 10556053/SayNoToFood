package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private TextView bt_return;
    private EditText et_name,et_email,et_password;
    private Button bt_register;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        et_email=(EditText)findViewById(R.id.et_email);
        et_name=(EditText)findViewById(R.id.et_name);
        et_password=(EditText)findViewById(R.id.et_password);
        bt_register=(Button)findViewById(R.id.bt_register);
        bt_return=(TextView)findViewById(R.id.bt_return);

        et_name.addTextChangedListener(registerTextWatcher);
        et_password.addTextChangedListener(registerTextWatcher);
        et_email.addTextChangedListener(registerTextWatcher);




        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = et_name.getText().toString();
                final String email = et_email.getText().toString();
                final String password= et_password.getText().toString();
                if (email.isEmpty()){
                    et_email.setError("email格式錯誤");
                    return;
                }if(password.isEmpty() ){
                    et_password.setError("請輸入密碼");
                    return;
                }
                else if (password.length()<6){
                    et_password.setError("密碼長度必須大於六字元");
                    return;
                }

                CollectionReference usersRef = fStore.collection("users");
                Query query = usersRef.whereEqualTo("name", name);//把所有等於et_name的欄位全部找出來

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                String user = documentSnapshot.getString("name");

                                if(user.equals(name)){
                                    Log.d(TAG, "User Exists");
                                    Toast.makeText(register.this, "Username exists", Toast.LENGTH_SHORT).show();
                                    et_name.setError("Username exists");
                                    return;
                                }else{
                                    break;
                                }
                            }
                        }
                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //===========================================================================//
                                    fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this, "成功創立帳戶，請到郵箱驗證信件", Toast.LENGTH_LONG).show();

                                                UserId=fAuth.getCurrentUser().getUid();
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("name", name);
                                                data.put("email", email);
                                                data.put("password", password);
                                                data.put("images", "");
                                                //======================================================================//
                                                DocumentReference documentReference=fStore.collection("users").document(UserId);//設定集合的名子為users,底下的文件以使用者id命名
                                                documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG,"success! useraccount is created for"+UserId);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG,"failed! "+e.toString());
                                                    }
                                                });
                                                //===================================================================//
                                            }else {
                                                Toast.makeText(register.this, "驗證郵件發送失敗"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    //===========================================================================//
                                } else {
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            };
                        });

                    }
                });

            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
    private TextWatcher registerTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = et_name.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            String email = et_email.getText().toString().trim();


            bt_register.setEnabled(!name.isEmpty() && !password.isEmpty()&& !email.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
