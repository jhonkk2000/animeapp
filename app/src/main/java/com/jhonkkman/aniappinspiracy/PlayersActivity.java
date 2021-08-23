package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;

import java.io.IOException;
import java.util.ArrayList;

import static com.jhonkkman.aniappinspiracy.AnimeActivity.anime_previous;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView rv_player;
    private AdapterPlayers adapter;
    private Toolbar toolbar;
    private ArrayList<String> videos = new ArrayList<>();
    private ArrayList<String> videosLat = new ArrayList<>();
    private ArrayList<String> videos_final = new ArrayList<>();
    private ArrayList<String> type = new ArrayList<>();
    private DatabaseReference dbr;
    private SharedPreferences pref;
    private InterstitialAd mInterstitialAd;
    private AppCompatButton btn_latino;
    private int episodio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        rv_player = findViewById(R.id.rv_players);
        toolbar = findViewById(R.id.toolbar_player);
        toolbar.setTitle(R.string.reproductor);
        videos = getIntent().getStringArrayListExtra("videos");
        btn_latino = findViewById(R.id.btn_latino);
        episodio = getIntent().getIntExtra("ep",0);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("user", MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        loadAd();
        try {
            loadPlayers();
            loadLatino();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLatino(){
        btn_latino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_latino.setEnabled(false);
                String btn = btn_latino.getText().toString();
                btn_latino.setText("Buscando...");
                loadAd();
                String[] anime_name = anime_previous.getUrl().split("/");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1],episodio);
                        apiVideoServer.setBASE_URL("https://henaojara.com/");
                        try {
                            videosLat = apiVideoServer.getVideoServers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(videosLat.size()!=0){
                            videos_final.addAll(videosLat);
                            for (int i = 0; i < videosLat.size(); i++) {
                                type.add("lat");
                            }
                            adapter.notifyDataSetChanged();
                            btn_latino.setEnabled(false);
                            btn_latino.getLayoutParams().height = 0;
                            btn_latino.requestLayout();
                        }else{
                            AlertUpdate alertUpdate = new AlertUpdate();
                            alertUpdate.alert = "play";
                            alertUpdate.showDialog(PlayersActivity.this);
                            alertUpdate.setTitulo("No disponible");
                            btn_latino.setText("No se encontro :(");
                            alertUpdate.setDesc("Vaya!, no hemos encontrado este anime en latino, pero puedes solicitarlo a traves de nuestro servidor de discord, y se agregara lo mas pronto posible, puedes encontrar" +
                                    " nuestro discord en el apartado de comunidad");
                        }
                    }
                },300);
            }
        });
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        //Interstitial
        InterstitialAd.load(this, getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(PlayersActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });


    }

    public void loadPlayers() throws IOException {
        ApiVideoServer apiVideoServer = new ApiVideoServer();
        videos_final = new ArrayList<>();
        type = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).startsWith("https://jkanime.net/um.php")) {
                String video = apiVideoServer.getDirectLink(videos.get(i));
                //Log.d("VideoPlayers",video);
                if (!video.equals("nada")) {
                    videos_final.add(video);
                    type.add("primary");
                }
            } else {
                if (videos.get(i).startsWith("https://jkanime.net/jk.php")) {
                    String video = apiVideoServer.getDirectLink(videos.get(i));
                    //Log.d("VideoPlayers",video);
                    if (!video.equals("nada")) {
                        videos_final.add(video);
                        type.add("primary");
                    }
                } else {
                    if (!videos.get(i).startsWith("https://jkanime.net/um2.php")) {
                        videos_final.add(videos.get(i));
                        type.add("secondary");
                    }
                }
            }
        }
        for (int i = 0; i < videos_final.size(); i++) {
            Log.d("VIDEOSFINAL:", videos_final.get(i));
        }
        rv_player.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPlayers(this, videos_final, dbr, pref.getString("id", ""), type);
        rv_player.setAdapter(adapter);
        try {
            AnimeActivity.dialog.dismissDialog();
        } catch (Exception e) {
        }
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