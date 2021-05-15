package com.jhonkkman.aniappinspiracy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.jhonkkman.aniappinspiracy.data.api.ApiClientData.getClient;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_register,btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_register = findViewById(R.id.btn_register_main);
        btn_login = findViewById(R.id.btn_login_main);
        onRegister();
        onLogin();
    }

    public void onRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FormActivity.class));
            }
        });
    }

    public void onLogin(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FormActivity.class));
            }
        });
    }

}