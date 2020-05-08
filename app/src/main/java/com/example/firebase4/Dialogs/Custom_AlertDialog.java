package com.example.firebase4.Dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.firebase4.FastEventScheduler.EventTimePicker;
import com.example.firebase4.FirstTimeInput.FirstTimeWeightInput;
import com.example.firebase4.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Custom_AlertDialog extends DialogFragment {

    private TextView content;
    private TextView title1;
    private Button alert_bt;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fast_alert_dialog,null);

        title1 = view.findViewById(R.id.tv_title1);
        content = view.findViewById(R.id.tv_content);
        alert_bt = view.findViewById(R.id.alert_bt);

        final Bundle bundle = getArguments();
        final int fastType = bundle.getInt("fastType");
        String[] title = {
                "16/8斷食法",
                "15/9斷食法",
                "14/10斷食法",
        };

        String[] description = {
                "在這裡放上斷食的一些注意事項",
                "在這裡放上斷食的一些注意事項",
                "在這裡放上斷食的一些注意事項",
        };

        builder.setView(view);
        content.setText(description[fastType]);
        title1.setText(title[fastType]);
        alert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("fastType",fastType);

                Intent i = new Intent(getApplicationContext(),EventTimePicker.class);
                i.putExtras(bundle1);
                startActivity(i);
            }
        });

        return builder.create();
    }
}
