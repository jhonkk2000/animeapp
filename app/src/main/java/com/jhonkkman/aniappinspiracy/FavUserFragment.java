package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhonkkman.aniappinspiracy.CenterActivity.user;
import static com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment.animesFav;

public class FavUserFragment extends Fragment {

    private RecyclerView rv_fav;
    private LinearLayoutManager lym;
    private AdapterAnimeFav adapter;
    private SharedPreferences pref;
    private ArrayList<AnimeResource> animes = new ArrayList<>();
    private ProgressBar pb_load;
    private int cont = 0;
    private int previous_size = 0;
    private DatabaseReference dbr;

    public FavUserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_user, container, false);
        rv_fav = view.findViewById(R.id.rv_fav_user);
        pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference();
        pb_load = view.findViewById(R.id.pb_fav_user);
        //loadIdsFavs();
        eventUpdate();
        return view;
    }

    public void eventUpdate(){
        dbr.child("users").child(pref.getString("id", "")).child("animes_fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FavoritoFragment.animesFav = (ArrayList<Long>) snapshot.getValue();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateFavs();
                        }
                    },1000);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void updateFavs() {
        Log.d("PREVIOUS", " " + previous_size + " - " + animesFav.size());
        if(previous_size>animesFav.size()){
            Log.d("PREVIOUS", " SI");
            cont = animesFav.size();
            ArrayList<AnimeResource> animesF = new ArrayList<>();
            for (int i = 0; i < animesFav.size(); i++) {
                for (int j = 0; j < animes.size(); j++) {
                    if(animesFav.get(i) == animes.get(j).getMal_id()){
                        animesF.add(animes.get(j));
                        break;
                    }
                }
            }
            animes = animesF;
            loadFavs();
        }else{
            Log.d("PREVIOUS", " NO");
            loadIdsFavs();
        }
        previous_size = animesFav.size();
    }

    @SuppressLint("NewApi")
    public void loadIdsFavs() {
        if(animesFav.size()>cont){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadAnime((animesFav.get(cont)));
                }
            },1500);
        }
    }

    public void loadAnime(long id) {
        ApiAnimeData API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Log.d("FUNCIONANDO","LLAMADAS");
        Call<AnimeResource> call = API_SERVICE.getAnime(id);
        call.enqueue(new Callback<AnimeResource>() {
            @Override
            public void onResponse(Call<AnimeResource> call, Response<AnimeResource> response) {
                if (response.isSuccessful()) {
                    AnimeResource anime = response.body();
                    animes.add(anime);
                    cont++;
                    if (cont == 1) {
                        loadFavs();
                    }
                    adapter.notifyDataSetChanged();
                    loadIdsFavs();
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadAnime(id);
                        }
                    }, 750);
                }
            }

            @Override
            public void onFailure(Call<AnimeResource> call, Throwable t) {

            }
        });
    }

    public void loadFavs() {
        pb_load.setVisibility(View.INVISIBLE);
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterAnimeFav(getContext(), getActivity(), animes, "Favs");
        rv_fav.setLayoutManager(lym);
        rv_fav.setAdapter(adapter);
        rv_fav.scheduleLayoutAnimation();
    }
}