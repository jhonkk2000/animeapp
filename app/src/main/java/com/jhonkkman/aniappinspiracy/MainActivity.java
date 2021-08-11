package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_register, btn_login;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verify();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.light_red));
        btn_register = findViewById(R.id.btn_register_main);
        btn_login = findViewById(R.id.btn_login_main);
        onRegister();
        onLogin();
    }

    public void verify() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            finish();
        }
    }

    public void onRegister() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SplashActivity.class).putExtra("register", false));
                finish();
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    public void onLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FormActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

}