package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AnimeFavActivity extends AppCompatActivity {

    private AppCompatButton btn_save;
    private RecyclerView rv_ani;
    private LinearLayoutManager lym;
    private AdapterAni adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_fav);
        btn_save = findViewById(R.id.btn_save_anime);
        rv_ani = findViewById(R.id.rv_ani_fav);
        onSave();
        loadAni();
    }

    public void onSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnimeFavActivity.this,GenFavActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    public void loadAni(){
        lym = new LinearLayoutManager(this);
        adapter = new AdapterAni(this);
        rv_ani.setLayoutManager(lym);
        rv_ani.setAdapter(adapter);
    }
}