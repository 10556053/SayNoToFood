package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class userPage extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final int PICK_IMAGE_REQUEST=1;

    private TextView tv_name,tv_email,tv_password,tv_url;
    private Button bt_logout;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
     private LoginManager loginManager;
    private GoogleSignInClient mGoogleSignInClientl;
    private String UserId ;
    private ImageView mImageView;
    private Uri mImageUri;
    private String loadUrl;


    private FirebaseStorage fStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        bt_logout=(Button)findViewById(R.id.bt_logout);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_email=(TextView)findViewById(R.id.tv_email);
        tv_password=(TextView)findViewById(R.id.tv_password);
        tv_url=(TextView)findViewById(R.id.tv_url);
        mImageView=(ImageView)findViewById(R.id.mImageView);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId=fAuth.getCurrentUser().getUid();
        loginManager = LoginManager.getInstance();
        fStorage= FirebaseStorage.getInstance();


        final StorageReference userIDRef= fStorage.getReference().child("User_Profile_Images").child(UserId+".jpg");

        DocumentReference documentReference=fStore.collection("users").document(UserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                tv_email.setText(documentSnapshot.getString("email"));
                tv_name.setText(documentSnapshot.getString("name"));
                tv_password.setText(documentSnapshot.getString("password"));
                loadUrl =documentSnapshot.getString("images");
                tv_url.setText(loadUrl);
                if(loadUrl.trim().length() ==0){
                    mImageView.setImageResource(R.drawable.avatar);
                }else{
                    Picasso.get().load(loadUrl).fit().into(mImageView);
                }

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
                loginManager.logOut();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

    }


    //============================開啟選擇圖片的介面============================//
    public  void SelectProfileImg(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fStorage= FirebaseStorage.getInstance();
        fAuth=FirebaseAuth.getInstance();
        UserId=fAuth.getCurrentUser().getUid();


        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null &&data.getData()!=null){
            //mImageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        //========================將檔案上傳=============================================//
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //result得到裁切完的圖片的資料
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                //用resultUri存放裁切完的圖片的uri
                final Uri resultUri= result.getUri();
                //檔案位址
                final StorageReference userIDRef= fStorage.getReference().child("User_Profile_Images").child(UserId+".jpg");

                //將檔案放入位址
                userIDRef.putFile(resultUri);
                DownloadViaUrl();
            }
        }

    }


    private void DownloadViaUrl() {

        StorageReference userIDRef= fStorage.getReference().child("User_Profile_Images").child(UserId+".jpg");
        UserId=fAuth.getCurrentUser().getUid();

        userIDRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                loadUrl = uri.toString();
                Map<String,Object> update= new HashMap<>();
                update.put("images",loadUrl);
                DocumentReference documentReference=fStore.collection("users").document(UserId);
                documentReference.set(update,SetOptions.merge());
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                        loadUrl =documentSnapshot.getString("images");
                        tv_url.setText(loadUrl);

                    }
                });
                LoadImage();
            }

            private void LoadImage() {
                Picasso.get().load(loadUrl).fit().into(mImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}
