package com.jhonkkman.aniappinspiracy.ui.configuracion;

import android.content.Intent;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.MainActivity;
import com.jhonkkman.aniappinspiracy.R;

public class ConfiguracionFragment extends Fragment {

    private AppCompatButton btn_cerrar_sesion;
    private FirebaseAuth mauth;
    private Switch sw_oscuro;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion);
        sw_oscuro = view.findViewById(R.id.sw_modo_oscuro);
        mAdView = view.findViewById(R.id.adView_configuracion);
        mauth = FirebaseAuth.getInstance();
        loadAd();
        onCerrarSesion();
        loadOscuro();
        return view;
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
    }

    public void onCerrarSesion(){
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                AlertLoading dialog = new AlertLoading();
                dialog.showDialog(getActivity(),"Cerrando sesión");
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