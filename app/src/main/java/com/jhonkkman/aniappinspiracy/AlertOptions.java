package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlertOptions {
    public Dialog dialog;
    EditText et_mensaje;
    AppCompatButton btn_ok, btn_cancel;
    public boolean send = false;

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_options);
        et_mensaje = dialog.findViewById(R.id.et_recommendation);
        btn_cancel = dialog.findViewById(R.id.btn_cancel_options);
        btn_ok = dialog.findViewById(R.id.btn_ok_options);
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
