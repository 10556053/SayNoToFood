package com.example.firebase4.FirstTimeInput;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.firebase4.MainActivity;
import com.example.firebase4.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TutorialVideo extends AppCompatActivity {

    private VideoView tutorial_video;
    private Button bt_next_page;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_video);

        tutorial_video=(VideoView)findViewById(R.id.tutorial_video);
        bt_next_page = (Button)findViewById(R.id.bt_next_page);

        final MediaController mediaController = new MediaController(this);
        //mediaController.setAnchorView(tutorial_video);

        storage = FirebaseStorage.getInstance();
        StorageReference imgRef= storage.getReference().child("videos").child("Produce_0.wmv");


        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                tutorial_video.setMediaController(mediaController);
                tutorial_video.setVideoURI(uri);

                tutorial_video.start();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        bt_next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialVideo.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}
