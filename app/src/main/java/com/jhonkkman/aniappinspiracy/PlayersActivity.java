package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;
import com.jhonkkman.aniappinspiracy.data.models.Extra;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import static com.jhonkkman.aniappinspiracy.AnimeActivity.anime_previous;
import static com.jhonkkman.aniappinspiracy.AnimeActivity.avaibleLat;
import static com.jhonkkman.aniappinspiracy.CenterActivity.PERMISSION_REQUEST_CODE;
import static com.jhonkkman.aniappinspiracy.CenterActivity.extras;

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
    private boolean cargaP = false;
    private ProgressBar pb_players;
    private String url = "-/-";
    private int epCount;
    private TextView tv_ep;
    private ImageButton btn_back, btn_forward;
    int state = 0;
    AlertLoading dialog = new AlertLoading();
    ArrayList<String> videosLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        rv_player = findViewById(R.id.rv_players);
        toolbar = findViewById(R.id.toolbar_player);
        toolbar.setTitle(R.string.reproductor);
        tv_ep = findViewById(R.id.tv_current_ep);
        btn_forward = findViewById(R.id.btn_forward_ep);
        btn_back = findViewById(R.id.btn_back_ep);
        videos = getIntent().getStringArrayListExtra("videos");
        btn_latino = findViewById(R.id.btn_latino);
        btn_latino.setVisibility(View.INVISIBLE);
        episodio = getIntent().getIntExtra("ep", 0);
        epCount = getIntent().getIntExtra("epC", 0);
        loadBtnLat();
        pb_players = findViewById(R.id.pb_players);
        if (getIntent().getStringExtra("urlA") != null) {
            url = getIntent().getStringExtra("urlA");
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("user", MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        loadAd();
        loadDataEp();
        try {
            loadPlayers(false, episodio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBtnLat() {
        if (avaibleLat) {
            btn_latino.setEnabled(true);
            btn_latino.setVisibility(View.VISIBLE);
            btn_latino.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
        } else {
            btn_latino.setEnabled(false);
            btn_latino.getLayoutParams().height = 0;
            btn_latino.setVisibility(View.INVISIBLE);
        }
    }

    public void loadChangeEpisode() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("episodesendcurrent", "" + episodio);
                loadEp(episodio - 1);
            }
        });
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("episodesendcurrent", "" + episodio);
                loadEp(episodio + 1);
            }
        });
    }

    public void loadEp(int position) {
        dialog.showDialog(this, "Cargando reproductores");
        final ArrayList<String>[] videos = new ArrayList[1];
        videos[0] = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                videos[0] = loadEpisode(position);
                videosLinks = videos[0];
            }
        }).start();
        updateData(position);
    }

    public void updateData(int newEp) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (state == 1) {
                    state = 0;
                    if (videosLinks.size() != 0 || avaibleLat) {
                        videos_final.clear();
                        videosLat.clear();
                        videos = videosLinks;
                        cargaP = false;
                        try {
                            loadPlayers(true, newEp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            dialog.dismissDialog();
                        } catch (Exception e) {
                        }
                        AlertUpdate alertUpdate = new AlertUpdate();
                        alertUpdate.alert = "play";
                        alertUpdate.showDialog(PlayersActivity.this);
                        alertUpdate.setTitulo("No disponible");
                        alertUpdate.btn_ok.setText("Aceptar");
                        alertUpdate.btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertUpdate.dismissDialog();
                            }
                        });
                        alertUpdate.setDesc("Capitulo no encontrado. Quizas aun no se estrena, de no ser asi, reportalo en nuestro servidor de discord");
                    }
                } else {
                    updateData(newEp);
                }
            }
        }, 1000);
    }

    public void loadDataEp() {
        tv_ep.setText("Episodio " + episodio);
        Log.d("COUNTEPTOTAL", "" + epCount);
        if (episodio == 1) {
            btn_back.setVisibility(View.INVISIBLE);
            btn_back.setEnabled(false);
        } else {
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setEnabled(true);
        }
        if (episodio < epCount) {
            btn_forward.setEnabled(true);
            btn_forward.setVisibility(View.VISIBLE);
        } else {
            btn_forward.setEnabled(false);
            btn_forward.setVisibility(View.INVISIBLE);
        }
    }

    public ArrayList<String> loadEpisode(int episode) {
        String[] anime_name = anime_previous.getUrl().split("/");
        ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1], episode);
        ArrayList<String> videos = new ArrayList<>();
        try {
            videos = apiVideoServer.getVideoServersSub();
            state = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videos;
    }

    public void loadLatino() {
        btn_latino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_latino.setEnabled(false);
                String btn = btn_latino.getText().toString();
                btn_latino.setText("Buscando...");
                loadAd();
                String[] anime_name = url.split("/");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1], episodio);
                        try {
                            videosLat = apiVideoServer.getVideoServersLat();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (videosLat.size() != 0) {
                            videos_final.addAll(videosLat);
                            for (int i = 0; i < videosLat.size(); i++) {
                                if (videosLat.get(i).endsWith(".mp4")) {
                                    type.add("latP");
                                } else {
                                    type.add("lat");
                                }
                            }
                            adapter.notifyDataSetChanged();
                            btn_latino.setEnabled(false);
                            btn_latino.getLayoutParams().height = 0;
                            btn_latino.requestLayout();
                        } else {
                            AlertUpdate alertUpdate = new AlertUpdate();
                            alertUpdate.alert = "play";
                            alertUpdate.showDialog(PlayersActivity.this);
                            alertUpdate.setTitulo("No disponible");
                            btn_latino.setText("No se encontro...");
                            alertUpdate.btn_ok.setText("Aceptar");
                            alertUpdate.btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertUpdate.dialog.dismiss();
                                }
                            });
                            alertUpdate.setDesc("Vaya!, no hemos encontrado este capitulo en latino, pero puedes solicitarlo a traves de nuestro servidor de discord, y se agregara lo mas pronto posible, puedes encontrar" +
                                    " nuestro discord en el apartado de comunidad");
                        }
                    }
                }, 300);
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
                    Log.d("TAGAD", "The interstitial ad wasn't ready yet.");
                }
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAGAD", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAGAD", "The ad failed to show.");
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

    public void loadPlayers(boolean update, int newEp) throws IOException {
        ApiVideoServer apiVideoServer = new ApiVideoServer();
        type = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    cargaP = true;
                } catch (IOException e) {
                }
            }
        }).start();
        for (int i = 0; i < videos_final.size(); i++) {
            Log.d("VIDEOSFINAL:", videos_final.get(i));
        }
        loadRecycler(update, newEp);
    }

    public void loadRecycler(boolean update, int newEp) {
        if (cargaP) {
            if (update) {
                try {
                    dialog.dismissDialog();
                } catch (Exception e) {
                }
                episodio = newEp;
                loadDataEp();
                //adapter.notifyDataSetChanged();
                if (avaibleLat) {
                    btn_latino.setVisibility(View.VISIBLE);
                    btn_latino.setText("Buscar en latino");
                    btn_latino.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    btn_latino.setEnabled(true);
                }
            }
            rv_player.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AdapterPlayers(this, videos_final, dbr, pref.getString("id", ""), type);
            rv_player.setAdapter(adapter);
            pb_players.setVisibility(View.INVISIBLE);
            loadChangeEpisode();
            loadLatino();

        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadRecycler(update, newEp);
                }
            }, 200);
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