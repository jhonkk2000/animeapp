package com.jhonkkman.aniappinspiracy.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jhonkkman.aniappinspiracy.AdapterAnimeImage;
import com.jhonkkman.aniappinspiracy.AdapterSeasonAnime;
import com.jhonkkman.aniappinspiracy.R;

public class InicioFragment extends Fragment {

    private RecyclerView rv_season,rv_continue;
    private AdapterSeasonAnime adapter;
    private AdapterAnimeImage adapter2;
    private LinearLayoutManager lym,lym2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        rv_season = root.findViewById(R.id.rv_season_anime);
        rv_continue = root.findViewById(R.id.rv_continue);
        loadSeason();
        loadContinue();
        return root;
    }

    public void loadSeason(){
        lym = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        adapter = new AdapterSeasonAnime();
        rv_season.setLayoutManager(lym);
        rv_season.setAdapter(adapter);
    }

    public void loadContinue(){
        lym2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        adapter2 = new AdapterAnimeImage();
        rv_continue.setLayoutManager(lym2);
        rv_continue.setAdapter(adapter2);
    }
}