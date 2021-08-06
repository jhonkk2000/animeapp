package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AlertLoading {

    Dialog dialog;
    TextView tv_mensaje;
    public boolean state = false;

    public void showDialog(Activity activity,String msj){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_loading);
        tv_mensaje = dialog.findViewById(R.id.tv_mensaje);
        tv_mensaje.setText(msj);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        state = true;
        dialog.show();
    }

    public void dismissDialog(){
        state = false;
        dialog.dismiss();
    }

}
