package com.example.firebase4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class userPage extends AppCompatActivity {
    private TextView tv_name,tv_email,tv_password;
    private Button bt_logout;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private GoogleSignInClient mGoogleSignInClientl;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        bt_logout=(Button)findViewById(R.id.bt_logout);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_email=(TextView)findViewById(R.id.tv_email);
        tv_password=(TextView)findViewById(R.id.tv_password);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fStore.collection("users").document(UserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                tv_email.setText(documentSnapshot.getString("email"));
                tv_name.setText(documentSnapshot.getString("name"));
                tv_password.setText(documentSnapshot.getString("password"));
            }
        });












        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClientl = GoogleSignIn.getClient(this, gso);

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                mGoogleSignInClientl.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

    }
}
