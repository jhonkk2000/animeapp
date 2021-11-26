package com.jhonkkman.aniappinspiracy.data.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.FoundAnimeActivity;
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientData {

    public static final String BASE_URL = "https://api.jikan.moe/v3/";
    private static Retrofit retrofit = null;
    public static int p = 0;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void cargarLlamada(int tipo, String busqueda, ApiAnimeData API_SERVICE, Context context, Activity activity, String type, AlertLoading dialog, int page) {
        switch (tipo) {
            case 0:
                loadCall(API_SERVICE.getAnimeSearch(busqueda, page), 0, context, activity, type, dialog, busqueda);
                break;
            case 1:
                loadCall(API_SERVICE.getAnimeLetter(busqueda, "score", "desc", page), 1, context, activity, type, dialog, busqueda);
                break;
            case 2:
                loadCall(API_SERVICE.getAnimeType(busqueda, "score", "desc", page), 2, context, activity, type, dialog, busqueda);
                break;
            case 3:
                loadCall(API_SERVICE.getAnimeRated(busqueda, "score", "desc", page), 3, context, activity, type, dialog, busqueda);
                break;
            case 4:
                loadCall(API_SERVICE.getAnimeScore(Float.parseFloat(busqueda), page), 4, context, activity, type, dialog, busqueda);
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
                                loadResultados(finalItems, 5, context, activity, type, dialog, busqueda);
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cargarLlamada(tipo, busqueda, API_SERVICE, context, activity, type, dialog, page);
                                }
                            }, 700);
                        }

                    }

                    @Override
                    public void onFailure(Call<AnimeGenResource> call, Throwable t) {
                    }
                });
                dialog.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callG.cancel();
                    }
                });
                break;
        }

    }

    public static void loadCall(Call<AnimeSearchRequest> callA, int pos, Context context, Activity activity, String type, AlertLoading dialog, String busqueda) {
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
                        if (!animeItems.get(i).getRated().equals("Rx") && !animeItems.get(i).getRated().equals("PG") && !animeItems.get(i).getType().equals("Music")
                                && !gen && !animeItems.get(i).getRated().equals("G")) {
                            finalItems.add(animeItems.get(i));
                        }
                    }
                    if (finalItems.size() != 0) {
                        loadResultados(finalItems, pos, context, activity, type, dialog, busqueda);
                    }

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadCall(callA.clone(), pos, context, activity, type, dialog, busqueda);
                        }
                    }, 700);
                }
            }

            @Override
            public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
            }
        });
        dialog.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                call.cancel();
            }
        });
    }

    public static void loadResultados(List<AnimeItem> animeItems, int pos, Context context, Activity activity, String type, AlertLoading dialog, String busqueda) {
        AnimeItem anime1 = animeItems.get(0);
        ArrayList<AnimeItem> lista1 = new ArrayList<>();
        ArrayList<AnimeItem> lista2 = new ArrayList<>();
        for (int i = 0; i < animeItems.size(); i++) {
            if (i != 0) {
                if (p == 0) {
                    lista1.add(animeItems.get(i));
                    p = 1;
                } else {
                    lista2.add(animeItems.get(i));
                    p = 0;
                }
            }
        }
        Intent intent = new Intent(context, FoundAnimeActivity.class);
        intent.putExtra("anime1", anime1);
        intent.putExtra("list1", lista1);
        intent.putExtra("list2", lista2);
        intent.putExtra("type", type);
        intent.putExtra("pos", pos);
        intent.putExtra("b", busqueda);
        context.startActivity(intent);
        //scroll.setVisibility(View.VISIBLE);
        dialog.dismissDialog();
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
}
