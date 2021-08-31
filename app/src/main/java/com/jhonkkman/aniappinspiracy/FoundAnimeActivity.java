package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhonkkman.aniappinspiracy.data.api.ApiClientData.p;

public class FoundAnimeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AnimeItem anime;
    private RecyclerView rv_search_items;
    public static AdapterResultados adapter;
    public static ApiAnimeData API_SERVICE;
    public static int page = 1;
    public static AlertLoading dialogL = new AlertLoading();
    public static ArrayList<AnimeItem> lista1 = new ArrayList<>();
    public static ArrayList<AnimeItem> lista2 = new ArrayList<>();
    public static ArrayList<AnimeItem> lista3 = new ArrayList<>();
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_anime);
        rv_search_items = findViewById(R.id.rv_more_results);
        toolbar = findViewById(R.id.toolbar_nav_foundanime);
        anime = (AnimeItem) getIntent().getSerializableExtra("anime1");
        toolbar.setTitle(getIntent().getStringExtra("type"));
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadAnimes();
        loadAd();
    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        //Interstitial
        InterstitialAd.load(this, getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FoundAnimeActivity.this);
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

    public void loadAnimes() {
        AnimeResource animeResource = new AnimeResource(anime.getMal_id(),anime.getEpisodes(),anime.getImage_url(),anime.getTitle(),anime.getType(),anime.getSource(),anime.getSynopsis(),anime.getUrl(),anime.isAiring(),anime.getScore(),anime.getGenres());
        lista1 = (ArrayList<AnimeItem>) getIntent().getSerializableExtra("list1");
        lista2 = (ArrayList<AnimeItem>) getIntent().getSerializableExtra("list2");
        lista3 = (ArrayList<AnimeItem>) getIntent().getSerializableExtra("list3");
        adapter = new AdapterResultados(lista1, lista2, lista3, this, this, "Found");
        adapter.setAnime(animeResource);
        rv_search_items.setLayoutManager(new LinearLayoutManager(this));
        rv_search_items.setAdapter(adapter);
    }

    public static void loadMoreAnimes(Activity context) {
        page++;
        dialogL.showDialog(context,"Cargando...");
        cargarLlamadaR(context.getIntent().getIntExtra("pos", 0), context.getIntent().getStringExtra("b"));
        /*btn_mas_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                dialogL.showDialog(FoundAnimeActivity.this,"Cargando...");
                cargarLlamadaR(getIntent().getIntExtra("pos", 0), getIntent().getStringExtra("b"));
            }
        });*/
    }

    public static void cargarLlamadaR(int tipo, String busqueda) {
        switch (tipo) {
            case 0:
                loadCallR(API_SERVICE.getAnimeSearch(busqueda, page), 0);
                break;
            case 1:
                loadCallR(API_SERVICE.getAnimeLetter(busqueda, "score", "desc", page), 1);
                break;
            case 2:
                loadCallR(API_SERVICE.getAnimeType(busqueda, "score", "desc", page), 2);
                break;
            case 3:
                loadCallR(API_SERVICE.getAnimeRated(busqueda, "score", "desc", page), 3);
                break;
            case 4:
                loadCallR(API_SERVICE.getAnimeScore(Float.parseFloat(busqueda), page), 4);
                break;
            case 5:
                //loadCall(API_SERVICE.getAnimeGenre(Integer.parseInt(busqueda)));
                Call<AnimeGenResource> callG = API_SERVICE.getGeneroAnime(Integer.parseInt(busqueda), page);
                callG.enqueue(new Callback<AnimeGenResource>() {
                    @Override
                    public void onResponse(Call<AnimeGenResource> call, Response<AnimeGenResource> response) {
                        if (response.isSuccessful()) {
                            List<AnimeItem> animeItems = response.body().getAnime();
                            List<AnimeItem> finalItems = new ArrayList<>();
                            for (int i = 0; i < animeItems.size(); i++) {
                                Log.d("CANTIDAD", animeItems.size() + "");
                                if (!animeItems.get(i).getType().equals("ONA") && !animeItems.get(i).getType().equals("Music")
                                        && !animeItems.get(i).isKids()) {
                                    finalItems.add(animeItems.get(i));
                                }
                                Log.d("CANTIDADF", finalItems.size() + "");
                            }
                            if (animeItems.size() != 0) {
                                loadResultadosR(finalItems, 5);
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cargarLlamadaR(tipo, busqueda);
                                }
                            }, 700);
                        }

                    }

                    @Override
                    public void onFailure(Call<AnimeGenResource> call, Throwable t) {
                    }
                });
                dialogL.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callG.cancel();
                    }
                });
                break;
        }

    }

    public static void loadCallR(Call<AnimeSearchRequest> callA, int pos) {
        Call<AnimeSearchRequest> call = callA;
        call.enqueue(new Callback<AnimeSearchRequest>() {
            @Override
            public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                if (response.isSuccessful()) {
                    List<AnimeItem> animeItems = response.body().getAnimeItems();
                    List<AnimeItem> finalItems = new ArrayList<>();
                    for (int i = 0; i < animeItems.size(); i++) {
                        boolean gen = false;
                        for (int j = 0; j < animeItems.get(i).getGenres().size(); j++) {
                            if (animeItems.get(i).getGenres().get(j).getMal_id() == 15) {
                                gen = true;
                                break;
                            }
                        }
                        if (!animeItems.get(i).getRated().equals("Rx") && !animeItems.get(i).getRated().equals("PG") && !animeItems.get(i).getType().equals("ONA") && !animeItems.get(i).getType().equals("Music")
                                && !gen && !animeItems.get(i).getRated().equals("G")) {
                            finalItems.add(animeItems.get(i));
                        }
                    }
                    if (finalItems.size() != 0) {
                        loadResultadosR(finalItems, pos);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadCallR(callA.clone(), pos);
                        }
                    }, 700);
                }
            }

            @Override
            public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
            }
        });
        dialogL.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                call.cancel();
            }
        });
    }

    public static void loadResultadosR(List<AnimeItem> animeItems, int pos) {
        for (int i = 0; i < animeItems.size(); i++) {
            if (p == 0) {
                lista1.add(animeItems.get(i));
                p = 1;
            } else {
                if (p == 1) {
                    lista2.add(animeItems.get(i));
                    p = 2;
                } else {
                    lista3.add(animeItems.get(i));
                    p = 0;
                }
            }
        }
        adapter.notifyDataSetChanged();
        dialogL.dismissDialog();
        /*TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        final Translator translator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(Void unused) {
                translator.translate(anime1.getSynopsis()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+SS:SS", Locale.ENGLISH);
                        LocalDate localDate;
                        if(pos==5){
                            localDate = LocalDate.parse(anime1.getAiring_start(), formatter);
                        }else{
                            localDate = LocalDate.parse(anime1.getStart_date(), formatter);
                        }
                        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        tv_fecha.setText("Fecha de estreno: " + sdf.format(date));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("TRADUCTOR","fallo");
            }
        });*/
        //rv_resultados.setNestedScrollingEnabled(false);
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