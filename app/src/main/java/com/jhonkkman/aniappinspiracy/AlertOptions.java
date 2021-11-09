package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlertOptions {
    public Dialog dialog;
    AppCompatButton btn_ok, btn_cancel;
    public boolean send = false;
    TextView tv_alert;

    public void showDialog(Activity activity,String msg){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_options);
        btn_cancel = dialog.findViewById(R.id.btn_cancel_options);
        btn_ok = dialog.findViewById(R.id.btn_ok_options);
        tv_alert = dialog.findViewById(R.id.tv_alert_options);
        tv_alert.setText(msg);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send = false;
                dismissDialog();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send = true;
                dismissDialog();
            }
        });
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
