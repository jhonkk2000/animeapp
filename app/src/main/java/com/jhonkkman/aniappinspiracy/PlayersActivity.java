package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;

import java.io.IOException;
import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView rv_player;
    private AdapterPlayers adapter;
    private Toolbar toolbar;
    private ArrayList<String> videos = new ArrayList<>();
    private DatabaseReference dbr;
    private SharedPreferences pref;

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
        pref = getSharedPreferences("user",MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        try {
            loadPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayers() throws IOException {
        ApiVideoServer apiVideoServer = new ApiVideoServer();
        ArrayList<String> videos_final = new ArrayList<>();
        ArrayList<String> type = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            if(videos.get(i).startsWith("https://jkanime.net/um.php")){
                String video = apiVideoServer.getDirectLink(videos.get(i));
                //Log.d("VideoPlayers",video);
                if(!video.equals("nada")){
                    videos_final.add(video);
                    type.add("primary");
                }
            }else{
                if(videos.get(i).startsWith("https://jkanime.net/jk.php")){
                    String video = apiVideoServer.getDirectLink(videos.get(i));
                    //Log.d("VideoPlayers",video);
                    if(!video.equals("nada")){
                        videos_final.add(video);
                        type.add("primary");
                    }
                }else{
                    if(!videos.get(i).startsWith("https://jkanime.net/um2.php")){
                        videos_final.add(videos.get(i));
                        type.add("secondary");
                    }

                }
            }
        }
        for (int i = 0; i < videos_final.size(); i++) {
                Log.d("VIDEOSFINAL:",videos_final.get(i));
        }
        rv_player.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPlayers(this,videos_final,dbr,pref.getString("id",""),type);
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