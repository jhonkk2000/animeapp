package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class AlertUpdate {

    Dialog dialog;
    private AppCompatButton btn_ok;
    SharedPreferences prefU;
    TextView tv_titulo,tv_desc;
    public String alert = "";

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_update);
        btn_ok = dialog.findViewById(R.id.btn_aceptar_update);
        tv_desc = dialog.findViewById(R.id.tv_desc_update);
        tv_titulo = dialog.findViewById(R.id.tv_titulo_update);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        prefU = activity.getSharedPreferences("update", Context.MODE_PRIVATE);
        dismissDialog();
        dialog.show();
    }

    public void setTitulo(String msg) {
        tv_titulo.setText(msg);
    }

    public void setDesc(String msg) {
        tv_desc.setText(msg);
    }

    public void dismissDialog(){
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alert.equals("com")){
                    SharedPreferences.Editor editor = prefU.edit();
                    editor.putBoolean("stateC",true);
                    editor.apply();
                    dialog.getContext().startActivity(new Intent(dialog.getContext(),ComunidadActivity.class));
                    dialog.dismiss();
                }else{
                    if(alert.equals("play")){
                        dialog.dismiss();
                    }else{
                        SharedPreferences.Editor editor = prefU.edit();
                        editor.putBoolean("state",true);
                        editor.apply();
                        dialog.dismiss();
                    }
                }
            }
        });
    }
}
