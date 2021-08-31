package com.jhonkkman.aniappinspiracy.ui.configuracion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.AlertRecommendation;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.MainActivity;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.SplashActivity;

public class ConfiguracionFragment extends Fragment {

    private AppCompatButton btn_cerrar_sesion,btn_send_r;
    private FirebaseAuth mauth;
    private Switch sw_oscuro;
    private AdView mAdView;
    private SharedPreferences pref,prefD,prefR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion);
        sw_oscuro = view.findViewById(R.id.sw_modo_oscuro);
        mAdView = view.findViewById(R.id.adView_configuracion);
        btn_send_r = view.findViewById(R.id.btn_send_recomm_config);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefD = getActivity().getSharedPreferences("darkMode",Context.MODE_PRIVATE);
        prefR = getActivity().getSharedPreferences("recommendation",Context.MODE_PRIVATE);
        mauth = FirebaseAuth.getInstance();
        loadAlertR();
        loadStateDark();
        loadNoLogin();
        //loadAd();
        onCerrarSesion();
        loadOscuro();
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void loadAlertR(){
        AlertRecommendation dialogR = new AlertRecommendation();
        if(prefR.getBoolean("send",false)){
            btn_send_r.setEnabled(false);
            btn_send_r.setText("Gracias :D");
            btn_send_r.setBackground(getResources().getDrawable(R.drawable.background_disable_minibutton));
        }
        btn_send_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogR.showDialog(getActivity());
                dialogR.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(dialogR.send){
                            btn_send_r.setEnabled(false);
                            btn_send_r.setText("Gracias :D");
                            btn_send_r.setBackground(getResources().getDrawable(R.drawable.background_disable_minibutton));
                        }
                    }
                });
            }
        });

    }

    public void loadStateDark(){
        sw_oscuro.setEnabled(false);
        int val = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(val==32){
            sw_oscuro.setChecked(true);
        }else{
            sw_oscuro.setChecked(false);
        }
    }

    public void loadNoLogin(){
        if(!CenterActivity.login){
            btn_cerrar_sesion.setEnabled(false);
            btn_cerrar_sesion.setText("No haz iniciado sesion");
        }
    }

    public void loadAd(){
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void loadOscuro(){
        sw_oscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw_oscuro.isChecked()){
                    changeState(true);
                    Intent i = new Intent(getContext(), SplashActivity.class);
                    if(!getActivity().getIntent().getBooleanExtra("login",true)){
                        i.putExtra("register",false);
                    }
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(i);
                    getActivity().finish();
                }else{
                    changeState(false);
                    Intent i = new Intent(getContext(), SplashActivity.class);
                    if(!getActivity().getIntent().getBooleanExtra("login",true)){
                        i.putExtra("register",false);
                    }
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
    }

    public void changeState(boolean val){
        SharedPreferences.Editor editor = prefD.edit();
        editor.putBoolean("night",val);
        editor.apply();
    }

    public void onCerrarSesion(){
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                AlertLoading dialog = new AlertLoading();
                dialog.showDialog(getActivity(),"Cerrando sesión");
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(null);
                editor.putString("usuario",json);
                editor.putString("id","");
                editor.apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getContext(), MainActivity.class));
                        dialog.dismissDialog();
                        getActivity().finish();
                        CenterActivity.centerActivity.finish();
                        System.exit(0);
                        cleanData();
                    }
                },2000);
            }
        });
    }

    public void cleanData(){
        CenterActivity.animesI.clear();
        CenterActivity.animesG.clear();
        CenterActivity.generosG.clear();
        CenterActivity.generos.clear();
        CenterActivity.animesGuardados.clear();
        CenterActivity.animesTop.clear();
        CenterActivity.season="";
        CenterActivity.avisos.clear();
        CenterActivity.animeItems.clear();
    }
}