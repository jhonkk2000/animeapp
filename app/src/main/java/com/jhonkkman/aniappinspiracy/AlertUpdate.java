package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class AlertUpdate {

    Dialog dialog;
    private AppCompatButton btn_ok;
    SharedPreferences prefU;

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_update);
        btn_ok = dialog.findViewById(R.id.btn_aceptar_update);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        prefU = activity.getSharedPreferences("update", Context.MODE_PRIVATE);
        dismissDialog();
        dialog.show();
    }

    public void dismissDialog(){
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefU.edit();
                editor.putBoolean("state",true);
                editor.apply();
                dialog.dismiss();
            }
        });
    }
}
