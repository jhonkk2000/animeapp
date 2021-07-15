package com.jhonkkman.aniappinspiracy.ui.inicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AdapterSeasonAnime;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopSeasonResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeWeekRequest;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.data.models.User;

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
    private AdapterResultados adapter2;
    private LinearLayoutManager lym, lym2;
    private ApiAnimeData API_SERVICE;
    private DatabaseReference dbr;
    private TextView tv_season;
    private ProgressBar pb_inicio;
    private ShimmerFrameLayout sm_season;
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
        rv_continue = root.findViewById(R.id.rv_generos_inicio);
        pb_inicio = root.findViewById(R.id.pb_inicio);
        tv_season = root.findViewById(R.id.tv_season_anime);
        dbr = FirebaseDatabase.getInstance().getReference();
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        sm_season = root.findViewById(R.id.sm_season);
        //sm_item.startShimmer();
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
                        if(animes.size()!=0){
                            in_1.setVisibility(View.INVISIBLE);
                            adapter2.notifyDataSetChanged();
                        }
                        if (finalL < animes.size() - 1) {
                            loadDataContinueLast();
                        }else{
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

    public void loadAnimeOfTheWeek(){
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
                            int val = 0;
                            for (int i = 0; i < animes.getMonday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getMonday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getMonday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getMonday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getTuesday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getTuesday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getTuesday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getTuesday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getWednesday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getWednesday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getWednesday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getWednesday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getThursday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getThursday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getThursday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getThursday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getFriday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getFriday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getFriday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getFriday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getSaturday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getSaturday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getSaturday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getSaturday().get(i));
                                        val = 0;
                                    }
                                }
                            }
                            for (int i = 0; i < animes.getSunday().size(); i++) {
                                if(val == 0){
                                    list1.add(animes.getSunday().get(i));
                                    val++;
                                }else{
                                    if(val == 1){
                                        list2.add(animes.getSunday().get(i));
                                        val++;
                                    }else{
                                        list3.add(animes.getSunday().get(i));
                                        val = 0;
                                    }
                                }
                            }
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
                            adapter2 = new AdapterResultados(list1,list2,list3,getContext(),getActivity(),"inicio");
                            rv_continue.setLayoutManager(lym2);
                            rv_continue.setAdapter(adapter2);
                            rv_continue.scheduleLayoutAnimation();
                            adapter2.notifyDataSetChanged();
                            pb_inicio.setVisibility(View.INVISIBLE);
                            if(!estado_seasion){
                                dialog.dismissDialog();
                            }
                        }else{
                            loadAnimeOfTheWeek();
                        }

                    }

                    @Override
                    public void onFailure(Call<AnimeWeekRequest> call, Throwable t) {
                        Log.d("NOCARGA", t.getMessage());
                    }
                });
        }, 1000);
    }
}