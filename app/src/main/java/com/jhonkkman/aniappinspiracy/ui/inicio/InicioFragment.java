package com.jhonkkman.aniappinspiracy.ui.inicio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.AdapterAnimeImage;
import com.jhonkkman.aniappinspiracy.AdapterInicioRv;
import com.jhonkkman.aniappinspiracy.AdapterSeasonAnime;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopSeasonResource;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhonkkman.aniappinspiracy.CenterActivity.animeItems;
import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;
import static com.jhonkkman.aniappinspiracy.CenterActivity.season;
import static com.jhonkkman.aniappinspiracy.CenterActivity.year;

public class InicioFragment extends Fragment {

    private RecyclerView rv_season, rv_continue;
    private AdapterSeasonAnime adapter;
    private AdapterInicioRv adapter2;
    private LinearLayoutManager lym, lym2;
    private ApiAnimeData API_SERVICE;
    private DatabaseReference dbr;
    private TextView tv_season;
    private ProgressBar pb_inicio;
    private ShimmerFrameLayout sm_season, sm_item;
    private SharedPreferences pref;
    private ArrayList<AnimeItem> animesI = new ArrayList<>();
    private ArrayList<ArrayList<AnimeItem>> animesG = new ArrayList<>();
    private User user;
    private List<GeneroItem> generosG = new ArrayList<>();
    private View in_1, in_2, in_3, in_4;
    private int finalI = -1;
    private int finalL = -1;
    private boolean estado_seasion = false;
    private boolean estado_genres = false;
    public static boolean estado_last=false;
    private AlertLoading dialog = new AlertLoading();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        rv_season = root.findViewById(R.id.rv_season_anime);
        rv_continue = root.findViewById(R.id.rv_rv_generos_inicio);
        pb_inicio = root.findViewById(R.id.pb_inicio);
        tv_season = root.findViewById(R.id.tv_season_anime);
        dbr = FirebaseDatabase.getInstance().getReference();
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        in_1 = root.findViewById(R.id.in_1);
        in_2 = root.findViewById(R.id.in_2);
        sm_season = root.findViewById(R.id.sm_season);
        sm_item = root.findViewById(R.id.sm_anime_item_gen_inicio);
        sm_item.startShimmer();
        sm_season.startShimmer();
        CenterActivity.animesI.clear();
        //Toast.makeText(getContext(), "a"+ finalI, Toast.LENGTH_SHORT).show();
        loadSeasonState();
        loadGenresState();
        loadUser();
        loadContinue();
        finalL = -1;
        loadDataSeason();
        setRetainInstance(true);
        return root;
    }

    public void loadSeasonState() {
        if (CenterActivity.animeItems.size() != 0) {
            estado_seasion = true;
        }else{
            dialog.showDialog(getActivity(),"Cargando animes!");
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
                                if(atsr.getAnime().get(i).getGenres().get(j).getMal_id()==15){
                                    gen = true;
                                    break;
                                }
                            }
                            if (!atsr.getAnime().get(i).getType().equals("Special") && !atsr.getAnime().get(i).getType().equals("Ona") && !atsr.getAnime().get(i).getType().equals("Music")
                            && !gen) {
                                CenterActivity.animeItems.add(atsr.getAnime().get(i));
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

                }
            });
        }
    }

    public void loadTitleSeason(String season, int year) {
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
        adapter2.notifyDataSetChanged();
    }

    public void loadContinue() {
        boolean lastAnimeView = false;
        if (user.getLast_anime_view().size() != 0) {
            lastAnimeView = true;
        }
        lym2 = new LinearLayoutManager(getContext());
        adapter2 = new AdapterInicioRv(getContext(), CenterActivity.generosG, getActivity(), lastAnimeView, CenterActivity.animesI, CenterActivity.animesG);
        if (lastAnimeView) {
            loadDataContinueLast();
        }else{
            loadDataGenres();
        }
        rv_continue.setLayoutManager(lym2);
        rv_continue.setAdapter(adapter2);
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
                        if(animes.size()!=0){
                            in_1.setVisibility(View.INVISIBLE);
                            adapter2.notifyDataSetChanged();
                        }
                        if (finalL < animes.size() - 1) {
                            loadDataContinueLast();
                        }else{
                            estado_last = true;
                            loadDataGenres();
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

    public void disablePb(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finalI = CenterActivity.generosG.size()-1;
                if (finalI < generos.size() - 1) {
                    disablePb();
                    //adapter2.notifyDataSetChanged();
                } else {
                    pb_inicio.setVisibility(View.INVISIBLE);
                }
            }
        },1500);
    }

    @SuppressLint("NewApi")
    public void loadDataGenres() {
        new Handler().postDelayed(() -> {
            if (estado_genres) {
                //adapter2.notifyDataSetChanged();
                //loadGenresState();
                finalI = CenterActivity.generosG.size()-1;
                if (finalI < generos.size() - 1) {
                    disablePb();
                } else {
                    pb_inicio.setVisibility(View.INVISIBLE);
                }
                in_1.setVisibility(View.INVISIBLE);
                in_2.setVisibility(View.INVISIBLE);
            } else {
                finalI++;
                Call<AnimeGenResource> call = API_SERVICE.getGeneroAnime(generos.get(finalI).getMal_id());
                call.enqueue(new Callback<AnimeGenResource>() {
                    @Override
                    public void onResponse(Call<AnimeGenResource> call, Response<AnimeGenResource> response) {
                        if (response.isSuccessful()) {
                            ArrayList<AnimeItem> animes = response.body().getAnime();
                            CenterActivity.animesG.add(animes);
                            CenterActivity.generosG.add(generos.get(finalI));
                            dialog.dismissDialog();
                            if(finalI%3==0){
                                adapter2.notifyDataSetChanged();
                            }
                            if (finalI < generos.size() - 1) {
                                loadDataGenres();
                            } else {
                                pb_inicio.setVisibility(View.INVISIBLE);
                            }
                            if (finalI == 0 && user.getLast_anime_view().size() != 0) {
                                in_2.setVisibility(View.INVISIBLE);
                            } else {
                                if (finalI == 0) {
                                    in_1.setVisibility(View.INVISIBLE);
                                } else {
                                    if (finalI == 1) {
                                        in_2.setVisibility(View.INVISIBLE);
                                        adapter2.notifyDataSetChanged();
                                    }
                                }
                            }
                        }else{
                            finalI--;
                            loadDataGenres();
                        }

                    }

                    @Override
                    public void onFailure(Call<AnimeGenResource> call, Throwable t) {
                        Log.d("NOCARGA", t.getMessage());
                        if (finalI <= generos.size()) {
                            loadDataGenres();
                        }
                    }
                });
            }
        }, 1000);
    }
}