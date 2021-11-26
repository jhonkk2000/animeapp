package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class AlertLoading {

    public Dialog dialog;
    TextView tv_mensaje;
    AppCompatButton btn_cancel;
    Handler handler;
    Runnable runnable;
    public boolean state = false;

    public void showDialog(Activity activity, String msj) {
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_loading);
        tv_mensaje = dialog.findViewById(R.id.tv_mensaje);
        btn_cancel = dialog.findViewById(R.id.btn_cancel_loading);
        //btn_cancel.setEnabled(false);
        tv_mensaje.setText(msj);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        state = true;
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(activity, "Vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
            }
        });
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                btn_cancel.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                btn_cancel.requestLayout();
            }
        };
        handler.postDelayed(runnable, 6000);
    }

    public void dismissDialog() {
        state = false;
        dialog.dismiss();
        handler.removeCallbacks(runnable);
    }

}
