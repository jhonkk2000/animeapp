package com.jhonkkman.aniappinspiracy.ui.configuracion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.MainActivity;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.SplashActivity;

public class ConfiguracionFragment extends Fragment {

    private AppCompatButton btn_cerrar_sesion;
    private FirebaseAuth mauth;
    private Switch sw_oscuro;
    private AdView mAdView;
    private SharedPreferences pref,prefD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion);
        sw_oscuro = view.findViewById(R.id.sw_modo_oscuro);
        mAdView = view.findViewById(R.id.adView_configuracion);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefD = getActivity().getSharedPreferences("darkMode",Context.MODE_PRIVATE);
        mauth = FirebaseAuth.getInstance();
        loadStateDark();
        loadNoLogin();
        loadAd();
        onCerrarSesion();
        loadOscuro();
        return view;
    }

    public void loadStateDark(){
        if(prefD.getBoolean("night",false)){
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
        CenterActivity.animeItems.clear();
    }
}