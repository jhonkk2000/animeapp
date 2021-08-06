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

public class AlertRecommendation {

    public Dialog dialog;
    EditText et_mensaje;
    AppCompatButton btn_send, btn_cancel;
    CheckBox chk_no_mostrar;
    SharedPreferences pref2;
    public boolean send = false;

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_recommendations);
        pref2 = activity.getSharedPreferences("recommendation", Context.MODE_PRIVATE);
        et_mensaje = dialog.findViewById(R.id.et_recommendation);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_send = dialog.findViewById(R.id.btn_send_recomm);
        chk_no_mostrar = dialog.findViewById(R.id.chk_no_mostrar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chk_no_mostrar.isChecked()){
                    SharedPreferences.Editor editor = pref2.edit();
                    editor.putBoolean("no_mostrar",true);
                    editor.putBoolean("send",false);
                    editor.apply();
                    dismissDialog();
                }else{
                    dismissDialog();
                }
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = et_mensaje.getText().toString();
                if(!mensaje.isEmpty() || mensaje.length()>=20){
                    SharedPreferences.Editor editor = pref2.edit();
                    editor.putBoolean("no_mostrar",true);
                    editor.putBoolean("send",true);
                    editor.apply();
                    DatabaseReference dbR = FirebaseDatabase.getInstance().getReference("recommendations");
                    dbR.push().child("mensaje").setValue(mensaje);
                    send = true;
                    dismissDialog();
                }else{
                    Toast.makeText(activity, "Ingresa una recomendacion de minimo 20 caracteres.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }


    public void dismissDialog(){
        dialog.dismiss();
    }
}
