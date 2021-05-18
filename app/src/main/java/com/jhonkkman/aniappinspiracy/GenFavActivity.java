package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GenFavActivity extends AppCompatActivity {

    private RecyclerView rv_gen;
    private LinearLayoutManager lym;
    private AdapterGen adapter;
    private AppCompatButton btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_fav);
        rv_gen = findViewById(R.id.rv_gen_fav);
        btn_save = findViewById(R.id.btn_save_gen);
        loadGen();
        onSave();
    }

    public void onSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GenFavActivity.this,CenterActivity.class));
            }
        });
    }

    public void loadGen(){
        lym = new LinearLayoutManager(this);
        adapter = new AdapterGen();
        rv_gen.setLayoutManager(lym);
        rv_gen.setAdapter(adapter);
    }
}