package com.example.firebase4.InputDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.firebase4.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class HeightInputDialog extends AppCompatDialogFragment implements NumberPicker.OnValueChangeListener {
    private NumberPicker hp;
    private TextView tv_display_cur_height;
    private HeightInputDialogListener heightInputDialogListener;



    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserId;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserId=fAuth.getCurrentUser().getUid();
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.heightpicker,null);
        hp=view.findViewById(R.id.height_picker);
        hp.setMaxValue(250);
        hp.setMinValue(100);
        hp.setValue(150);
        hp.setWrapSelectorWheel(false);
        hp.setOnValueChangedListener(this);
        tv_display_cur_height=view.findViewById(R.id.tv_display_cur_height);

        builder.setView(view);
        builder.setTitle("Pick ur weight");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i = hp.getValue();
                String height = Integer.toString(i);
                heightInputDialogListener.applyHeight(height);
                DocumentReference documentReference = fStore.collection("users").document(UserId).collection("bodyData").document("my_body_data");
                Map<String, Object> myBodyData = new HashMap<>();
                myBodyData.put("current_height", i);
                documentReference.set(myBodyData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //ctrl + alt + t 自動生成try_catch
        try {
            heightInputDialogListener=(HeightInputDialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+"must implement WeightInputDialogListener");
        }
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        tv_display_cur_height.setText(newVal+"公分");
    }

    public interface HeightInputDialogListener{
        void applyHeight(String height);
    }
}
