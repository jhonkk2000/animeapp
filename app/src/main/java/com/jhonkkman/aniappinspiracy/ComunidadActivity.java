package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class ComunidadActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatButton btn_unete;
    private RecyclerView rv_avisos;
    private AdapterAviso adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad);
        toolbar = findViewById(R.id.toolbar_nav_comunidad);
        btn_unete = findViewById(R.id.btn_unete);
        rv_avisos = findViewById(R.id.rv_avisos);
        toolbar.setTitle("Comunidad");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadUnete();
        loadAvisos();
    }

    public void loadAvisos(){
        adapter = new AdapterAviso(this);
        rv_avisos.setLayoutManager(new LinearLayoutManager(this));
        rv_avisos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void loadUnete(){
        btn_unete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/wMuSBKcUmY")));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}