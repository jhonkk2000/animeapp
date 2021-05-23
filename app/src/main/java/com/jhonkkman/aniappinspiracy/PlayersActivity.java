package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView rv_player;
    private AdapterPlayers adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        rv_player = findViewById(R.id.rv_players);
        toolbar = findViewById(R.id.toolbar_player);
        toolbar.setTitle(R.string.reproductor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadPlayers();
    }

    public void loadPlayers(){
        rv_player.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPlayers();
        rv_player.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}