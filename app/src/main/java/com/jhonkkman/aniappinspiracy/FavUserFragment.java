package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavUserFragment extends Fragment {

    private RecyclerView rv_fav;
    private LinearLayoutManager lym;
    private AdapterAnimeFav adapter;
    private List<Long> animesFav;
    private SharedPreferences pref;
    private ArrayList<AnimeResource> animes = new ArrayList<>();
    private ProgressBar pb_load;
    private int cont = 0;

    public FavUserFragment(List<Long> animesFav){
        this.animesFav = animesFav;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_user, container, false);
        rv_fav = view.findViewById(R.id.rv_fav_user);
        pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        pb_load = view.findViewById(R.id.pb_fav_user);
        loadIdsFavs();
        return view;
    }

    @SuppressLint("NewApi")
    public void loadIdsFavs(){
        Gson gson = new Gson();
        String json = pref.getString("usuario","");
        User user = gson.fromJson(json,User.class);
        //animesFav = user.getAnime_fav();
        for (int i = 0; i < animesFav.size(); i++) {
            loadAnime(Math.toIntExact((animesFav.get(i))));
        }
        //loadFavs();
    }

    public void loadAnime(int id){
        ApiAnimeData API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<AnimeResource> call = API_SERVICE.getAnime(id);
        call.enqueue(new Callback<AnimeResource>() {
            @Override
            public void onResponse(Call<AnimeResource> call, Response<AnimeResource> response) {
                if(response.isSuccessful()){
                    AnimeResource anime = response.body();
                    animes.add(anime);
                    cont++;
                    if(cont==1){
                        loadFavs();
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadAnime(id);
                        }
                    },750);
                }
            }

            @Override
            public void onFailure(Call<AnimeResource> call, Throwable t) {

            }
        });
    }

    public void loadFavs(){
        pb_load.setVisibility(View.INVISIBLE);
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterAnimeFav(getContext(),getActivity(),animes);
        rv_fav.setLayoutManager(lym);
        rv_fav.setAdapter(adapter);
        rv_fav.scheduleLayoutAnimation();
    }
}