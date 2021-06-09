package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;

import java.io.IOException;
import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView rv_player;
    private AdapterPlayers adapter;
    private Toolbar toolbar;
    private ArrayList<String> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        rv_player = findViewById(R.id.rv_players);
        toolbar = findViewById(R.id.toolbar_player);
        toolbar.setTitle(R.string.reproductor);
        videos = getIntent().getStringArrayListExtra("videos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            loadPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayers() throws IOException {
        ApiVideoServer apiVideoServer = new ApiVideoServer();
        ArrayList<String> videos_final = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            String video = apiVideoServer.getDirectLink(videos.get(i));
            if(!video.equals("nada")){
                videos_final.add(video);
            }
        }
        rv_player.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPlayers(this,videos_final);
        rv_player.setAdapter(adapter);
        AnimeActivity.dialog.dismissDialog();
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