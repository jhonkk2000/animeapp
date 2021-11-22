package com.jhonkkman.aniappinspiracy.ui.inicio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AdapterSeasonAnime;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.AlertOptions;
import com.jhonkkman.aniappinspiracy.AlertRecommendation;
import com.jhonkkman.aniappinspiracy.AlertUpdate;
import com.jhonkkman.aniappinspiracy.AnimeActivity;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.ComunidadActivity;
import com.jhonkkman.aniappinspiracy.DownloadTask;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopSeasonResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeWeekRequest;
import com.jhonkkman.aniappinspiracy.data.models.Aviso;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.jhonkkman.aniappinspiracy.data.update.NewUpdate;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhonkkman.aniappinspiracy.CenterActivity.PERMISSION_REQUEST_CODE;
import static com.jhonkkman.aniappinspiracy.CenterActivity.avisos;
import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;
import static com.jhonkkman.aniappinspiracy.CenterActivity.update_gp;
import static com.jhonkkman.aniappinspiracy.CenterActivity.urlApkDownload;

public class InicioFragment extends Fragment {

    private RecyclerView rv_season, rv_continue;
    private AdapterSeasonAnime adapter;
    private AdapterResultados adapter2;
    private LinearLayoutManager lym, lym2;
    private ImageView iv_carousel;
    private ApiAnimeData API_SERVICE;
    private DatabaseReference dbr;
    private TextView tv_season, tv_titulo, tv_desc;
    private ProgressBar pb_inicio;
    private ShimmerFrameLayout sm_season;
    private AppCompatButton btn_acceder;
    private SharedPreferences pref, prefR, prefU;
    private ArrayList<AnimeItem> animesI = new ArrayList<>();
    private ArrayList<ArrayList<AnimeItem>> animesG = new ArrayList<>();
    private ArrayList<AnimeItem> animeOfDay = new ArrayList<>();
    private User user;
    private List<GeneroItem> generosG = new ArrayList<>();
    private View in_1, in_2, in_3, in_4;
    private int finalI = -1;
    private int finalL = -1;
    private boolean estado_seasion = false;
    private boolean estado_genres = false;
    private String season;
    public static boolean estado_last = false;
    public List<AnimeItem> animeItems = new ArrayList<>();
    private AlertLoading dialog = new AlertLoading();
    private int carouselCount = 0, year;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        rv_season = root.findViewById(R.id.rv_season_anime);
        btn_acceder = root.findViewById(R.id.btn_acceder_carousel);
        rv_continue = root.findViewById(R.id.rv_generos_inicio);
        iv_carousel = root.findViewById(R.id.iv_carousel);
        pb_inicio = root.findViewById(R.id.pb_inicio);
        tv_season = root.findViewById(R.id.tv_season_anime);
        tv_titulo = root.findViewById(R.id.tv_titulo_carousel);
        tv_desc = root.findViewById(R.id.tv_desc_carousel);
        season = getArguments().getString("season");
        year = getArguments().getInt("year");
        dbr = FirebaseDatabase.getInstance().getReference();
        prefR = getActivity().getSharedPreferences("recommendation", Context.MODE_PRIVATE);
        prefU = getActivity().getSharedPreferences("update", Context.MODE_PRIVATE);
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        sm_season = root.findViewById(R.id.sm_season);
        sm_season.startShimmer();
        CenterActivity.animesI.clear();
        openAnimeCarousel();
        loadSeasonState();
        loadGenresState();
        loadUser();
        loadContinue();
        finalL = -1;
        loadDataSeason();

        //setRetainInstance(true);
        return root;
    }

    public void loadCarousel() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (carouselCount) {
                    case 0:
                        loadAnimationCarousel(0);
                        carouselCount++;
                        break;
                    case 1:
                        loadAnimationCarousel(1);
                        carouselCount++;
                        break;
                    case 2:
                        loadAnimationCarousel(2);
                        carouselCount++;
                        break;
                    case 3:
                        loadAnimationCarousel(3);
                        carouselCount++;
                        break;
                    case 4:
                        loadAnimationCarousel(4);
                        carouselCount = 0;
                        break;
                }

            }
        }, 5000);
    }

    public void loadAnimationCarousel(int carouselCount) {
        ImageViewAnimatedChange(iv_carousel, animeOfDay.get(carouselCount).getImage_url(), animeOfDay.get(carouselCount).getTitle(), animeOfDay.get(carouselCount).getSynopsis());
        loadCarousel();
    }

    public void ImageViewAnimatedChange(final ImageView v, String url, String title, String desc) {
        try {
            Log.d("VISIBILIDAD", "visible" + " " + title);
            final Animation anim_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
            final Animation anim_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Glide.with(getActivity().getApplicationContext()).load(url).into(v);
                    tv_titulo.setText(title);
                    tv_desc.setText(desc);
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }
                    });
                    v.startAnimation(anim_in);
                    btn_acceder.setAnimation(anim_in);
                    tv_titulo.setAnimation(anim_in);
                    tv_desc.setAnimation(anim_in);
                }
            });
            v.startAnimation(anim_out);
            btn_acceder.setAnimation(anim_out);
            tv_titulo.startAnimation(anim_out);
            tv_desc.setAnimation(anim_out);

        } catch (Exception e) {
        }

    }

    public void openAnimeCarousel() {
        btn_acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AnimeActivity.class);
                if (carouselCount != 0) {
                    i.putExtra("anime", (Serializable) animeOfDay.get(carouselCount - 1));
                } else {
                    i.putExtra("anime", (Serializable) animeOfDay.get(4));
                }
                //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), cv_2, ViewCompat.getTransitionName(cv_2));
                getContext().startActivity(i);
            }
        });
    }

    public void loadSeasonState() {
        if (animeItems.size() != 0) {
            estado_seasion = true;
        } else {
            dialog.showDialog(getActivity(), "Cargando animes!");
        }
    }

    public void loadGenresState() {
        if (CenterActivity.generosG.size() != 0) {
            estado_genres = true;
        }
    }

    public void loadUser() {
        Gson gson = new Gson();
        String json = pref.getString("usuario", "");
        user = gson.fromJson(json, User.class);
    }

    public void loadDataSeason() {
        loadTitleSeason(season, year);
        if (estado_seasion) {
            sm_season.stopShimmer();
            sm_season.setVisibility(View.INVISIBLE);
            loadRvSeason();
        } else {
            Call<AnimeTopSeasonResource> call = API_SERVICE.getAnimeTopSeason(year, season);
            call.enqueue(new Callback<AnimeTopSeasonResource>() {
                @Override
                public void onResponse(Call<AnimeTopSeasonResource> call, Response<AnimeTopSeasonResource> response) {
                    if (response.isSuccessful()) {
                        AnimeTopSeasonResource atsr = response.body();
                        for (int i = 0; i < atsr.getAnime().size(); i++) {
                            boolean gen = false;
                            for (int j = 0; j < atsr.getAnime().get(i).getGenres().size(); j++) {
                                if (atsr.getAnime().get(i).getGenres().get(j).getMal_id() == 15 || atsr.getAnime().get(i).getGenres().get(j).getMal_id() == 12) {
                                    gen = true;
                                    break;
                                }
                            }
                            if (!atsr.getAnime().get(i).getType().equals("Special") && !atsr.getAnime().get(i).getType().equals("Ona") && !atsr.getAnime().get(i).getType().equals("Music")
                                    && !gen) {
                                //Log.d("INICIOEPISODESIZE",""+atsr.getAnime().get(i).getEpisodes() + atsr.getAnime().get(i).getTitle());
                                animeItems.add(atsr.getAnime().get(i));
                            }
                        }
                        sm_season.stopShimmer();
                        sm_season.setVisibility(View.INVISIBLE);
                    }
                    Log.d("PESOOOO", "" + animeItems.size());
                    loadRvSeason();
                }

                @Override
                public void onFailure(Call<AnimeTopSeasonResource> call, Throwable t) {
                    Log.d("NOCARGASEASON", t.toString());
                }
            });
        }
    }

    public void loadTitleSeason(String season, int year) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        switch (season) {
            case "summer":
                tv_season.setText("Verano " + year);
                break;
            case "winter":
                tv_season.setText("Invierno " + year);
                break;
            case "spring":
                tv_season.setText("Primavera " + year);
                break;
            case "fall":
                tv_season.setText("Otoño " + year);
                break;
        }
    }

    public void loadRvSeason() {
        lym = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new AdapterSeasonAnime(this.getContext(), animeItems, getActivity());
        adapter.notifyDataSetChanged();
        rv_season.setLayoutManager(lym);
        rv_season.setAdapter(adapter);
        rv_season.setNestedScrollingEnabled(false);
    }

    public void loadContinue() {
        loadAnimeOfTheWeek();
    }

    public void loadDataContinueLast() {
        new Handler().postDelayed(() -> {
            finalL++;
            List<Integer> animes = user.getLast_anime_view();
            List<Integer> newanimes = new ArrayList<>();
            for (int i = 1; i < animes.size() + 1; i++) {
                newanimes.add(animes.get(animes.size() - i));
            }
            //animeItems.clear();
            Call<AnimeResource> call = API_SERVICE.getAnime(newanimes.get(finalL));
            call.enqueue(new Callback<AnimeResource>() {
                @Override
                public void onResponse(Call<AnimeResource> call, Response<AnimeResource> response) {
                    if (response.isSuccessful()) {
                        AnimeResource anime = response.body();
                        AnimeItem a = new AnimeItem(anime.getMal_id(), anime.getTitle(), anime.getImage_url());
                        a.setUrl(anime.getUrl());
                        CenterActivity.animesI.add(a);
                        if (animes.size() != 0) {
                            in_1.setVisibility(View.INVISIBLE);
                            adapter2.notifyDataSetChanged();
                        }
                        if (finalL < animes.size() - 1) {
                            loadDataContinueLast();
                        } else {
                            estado_last = true;
                        }
                    } else {
                        finalL--;
                        Log.d("NOCARGA", "no response last");
                        loadDataContinueLast();
                    }

                }

                @Override
                public void onFailure(Call<AnimeResource> call, Throwable t) {
                    Log.d("NOCARGA", t.getMessage());
                }
            });
        }, 700);
    }

    public void disablePb() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finalI = CenterActivity.generosG.size() - 1;
                if (finalI < generos.size() - 1) {
                    disablePb();
                    //adapter2.notifyDataSetChanged();
                } else {
                    pb_inicio.setVisibility(View.INVISIBLE);
                }
            }
        }, 1500);
    }

    public String getDayEnglish(int d) {
        String day = "";
        switch (d) {
            case 2:
                day = "sunday";
                break;
            case 3:
                day = "monday";
                break;
            case 4:
                day = "tuesday";
                break;
            case 5:
                day = "wednesday";
                break;
            case 6:
                day = "thursday";
                break;
            case 7:
                day = "friday";
                break;
            case 1:
                day = "saturday";
                break;
        }
        return day;
    }

    public void loadAnimeOfTheWeek() {
        new Handler().postDelayed(() -> {
            Call<AnimeWeekRequest> call = API_SERVICE.getAnimeWeek();
            call.enqueue(new Callback<AnimeWeekRequest>() {
                @Override
                public void onResponse(Call<AnimeWeekRequest> call, Response<AnimeWeekRequest> response) {
                    if (response.isSuccessful()) {
                        AnimeWeekRequest animes = response.body();
                        ArrayList<AnimeItem> list1 = new ArrayList<>();
                        ArrayList<AnimeItem> list2 = new ArrayList<>();
                        ArrayList<AnimeItem> list3 = new ArrayList<>();
                        Calendar c = Calendar.getInstance();
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        String day = getDayEnglish(dayOfWeek);
                        int val = 0;
                        for (int i = 0; i < animes.getMonday().size(); i++) {
                            Log.d("PROBANDOLAST", "" + animes.getMonday().get(i).getGenres().size());
                            if (animes.getMonday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("monday")) {
                                    animeOfDay.add(animes.getMonday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getMonday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getMonday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getMonday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < animes.getTuesday().size(); i++) {
                            if (animes.getTuesday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("tuesday")) {
                                    animeOfDay.add(animes.getTuesday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getTuesday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getTuesday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getTuesday().get(i));
                                        val = 0;
                                    }
                                }
                            }

                        }
                        for (int i = 0; i < animes.getWednesday().size(); i++) {
                            if (animes.getWednesday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("wednesday")) {
                                    animeOfDay.add(animes.getWednesday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getWednesday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getWednesday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getWednesday().get(i));
                                        val = 0;
                                    }
                                }
                            }

                        }
                        for (int i = 0; i < animes.getThursday().size(); i++) {
                            if (animes.getThursday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("thursday")) {
                                    animeOfDay.add(animes.getThursday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getThursday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getThursday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getThursday().get(i));
                                        val = 0;
                                    }
                                }
                            }

                        }
                        for (int i = 0; i < animes.getFriday().size(); i++) {
                            if (animes.getFriday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("Friday")) {
                                    animeOfDay.add(animes.getFriday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getFriday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getFriday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getFriday().get(i));
                                        val = 0;
                                    }
                                }
                            }

                        }
                        for (int i = 0; i < animes.getSaturday().size(); i++) {
                            if (animes.getSaturday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("saturday")) {
                                    animeOfDay.add(animes.getSaturday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getSaturday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getSaturday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getSaturday().get(i));
                                        val = 0;
                                    }
                                }
                            }

                        }
                        for (int i = 0; i < animes.getSunday().size(); i++) {
                            Log.d("TAGANIME", animes.getSunday().get(i).getTitle());
                            if (animes.getSunday().get(i).getGenres().get(0).getMal_id() != 12) {
                                if (day.equalsIgnoreCase("sunday")) {
                                    animeOfDay.add(animes.getSunday().get(i));
                                }
                                if (val == 0) {
                                    list1.add(animes.getSunday().get(i));
                                    val++;
                                } else {
                                    if (val == 1) {
                                        list2.add(animes.getSunday().get(i));
                                        val++;
                                    } else {
                                        list3.add(animes.getSunday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                        }
                        Collections.sort(animeOfDay, new Comparator<AnimeItem>() {
                            @Override
                            public int compare(AnimeItem o1, AnimeItem o2) {
                                return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
                            }
                        });
                        Collections.sort(list1, new Comparator<AnimeItem>() {
                            @Override
                            public int compare(AnimeItem o1, AnimeItem o2) {
                                return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
                            }
                        });
                        Collections.sort(list2, new Comparator<AnimeItem>() {
                            @Override
                            public int compare(AnimeItem o1, AnimeItem o2) {
                                return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
                            }
                        });
                        Collections.sort(list3, new Comparator<AnimeItem>() {
                            @Override
                            public int compare(AnimeItem o1, AnimeItem o2) {
                                return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
                            }
                        });
                        lym2 = new LinearLayoutManager(getContext());
                        adapter2 = new AdapterResultados(list1, list2, list3, getContext(), getActivity(), "inicio");
                        rv_continue.setLayoutManager(lym2);
                        rv_continue.setAdapter(adapter2);
                        rv_continue.scheduleLayoutAnimation();
                        adapter2.notifyDataSetChanged();
                        pb_inicio.setVisibility(View.INVISIBLE);
                        if (!estado_seasion) {
                            dialog.dismissDialog();
                            if (!prefU.getBoolean("state", false)) {
                                AlertUpdate alertUpdate = new AlertUpdate();
                                alertUpdate.showDialog(getActivity());
                            }
                        }
                        if (!prefR.getBoolean("no_mostrar", false)) {
                            AlertRecommendation dialogR = new AlertRecommendation();
                            dialogR.showDialog(getActivity());
                        }
                        iv_carousel.setVisibility(View.VISIBLE);
                        tv_desc.setVisibility(View.VISIBLE);
                        tv_titulo.setVisibility(View.VISIBLE);
                        btn_acceder.setVisibility(View.VISIBLE);
                        Log.d("PESODEANIMEOFDAY", "" + dayOfWeek);
                        if (!CenterActivity.carousel_state) {
                            CenterActivity.carousel_state = true;
                            ImageViewAnimatedChange(iv_carousel, animeOfDay.get(0).getImage_url(), animeOfDay.get(0).getTitle(), animeOfDay.get(0).getSynopsis());
                            carouselCount = 1;
                            loadCarousel();
                        }
                        if(update_gp){
                            NewUpdate newUpdate = new NewUpdate(getActivity(),urlApkDownload);
                            newUpdate.loadDialogSecondApp();
                        }
                        loadAnnouncement();
                    } else {
                        loadAnimeOfTheWeek();
                    }

                }

                @Override
                public void onFailure(Call<AnimeWeekRequest> call, Throwable t) {
                    Log.d("NOCARGA", t.getCause() + " - " + t.getMessage());
                }
            });
        }, 1000);
    }

    public void loadAnnouncement(){
            if(avisos != null){
                for (int i = 0; i < avisos.size(); i++) {
                    if(avisos.get(i).getTitulo().startsWith("$")){
                        Aviso aviso = avisos.get(i);
                        AlertOptions alert = new AlertOptions();
                        alert.showDialog(getActivity(),aviso.getTitulo());
                        alert.loadImage(getContext(),aviso.getImagen());
                        alert.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(alert.send){
                                    startActivity(new Intent(getContext(), ComunidadActivity.class));
                                }
                            }
                        });
                        break;
                    }
                }
            }

    }
}