package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText et_email,et_password;
    private Button bt_google ,bt_login;
    private TextView bt_create_account;
    private int RC_SIGN_IN = 1;
    String TAG="TAG";
    private String Userid;
    private LoginButton loginButton;


    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private GoogleSignInClient mGoogleSignInClientl;

    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_google=(Button)findViewById(R.id.bt_google);
        bt_create_account=(TextView)findViewById(R.id.bt_create_account);


        fAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseUser currentUser = fAuth.getCurrentUser();
        mCallbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
    //=========================Email and Password===================================//
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= et_email.getText().toString();
                String password= et_password.getText().toString();

                if (email.isEmpty()){
                    et_email.setError("請輸入信箱");
                    return;
                }
                if (password.isEmpty()|| password.length()<6){
                    et_password.setError("密碼錯誤");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(fAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "go to mailbox to checkout varification email", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(getApplicationContext(),userPage.class));
                        }else{
                            Toast.makeText(MainActivity.this, "error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //======================Facebook============================//
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        //======================Google==============================//
        //=========================Google setup===========================//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClientl = GoogleSignIn.getClient(this, gso);
        bt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //=================================================================//
        bt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });


    }


    //==============Google SignIn Functions========================//
    private void signIn() {
        Intent signInIntent = mGoogleSignInClientl.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        fStore=FirebaseFirestore.getInstance();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            //Uri personPhoto = acct.getPhotoUrl();
            Userid= user.getUid();
            Map<String, Object> data = new HashMap<>();
            data.put("name", personName);
            data.put("email", personEmail);
            data.put("password", personId);
            DocumentReference documentReference=fStore.collection("users").document(Userid);//設定集合的名子為users,底下的文件以使用者id命名
            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"success! useraccount is created for"+Userid);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"failed! "+e.toString());
                }
            });
            startActivity(new Intent(getApplicationContext(),userPage.class));
        }
    }
    //==============FaceBook SignIn Functions========================//
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateFaceBookUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateFaceBookUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateFaceBookUI(FirebaseUser user){
        fStore=FirebaseFirestore.getInstance();
        if(user!= null ){
            String personName = user.getDisplayName();
            String personEmail = user.getEmail();
            Userid= user.getUid();
            Map<String, Object> data = new HashMap<>();
            data.put("name", personName);
            data.put("email", personEmail);

            DocumentReference documentReference=fStore.collection("users").document(Userid);//設定集合的名子為users,底下的文件以使用者id命名
            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"success! useraccount is created for"+Userid);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"failed! "+e.toString());
                }
            });
            startActivity(new Intent(getApplicationContext(),userPage.class));
        }
    }
}
