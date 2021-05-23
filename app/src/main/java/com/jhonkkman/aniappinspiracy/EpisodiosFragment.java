package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EpisodiosFragment extends Fragment {

    private RecyclerView rv_episodios;
    private AdapterEpisodio adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episodios, container, false);
        rv_episodios = view.findViewById(R.id.rv_episodios);
        loadEpisodes();
        return view;
    }

    public void loadEpisodes(){
        rv_episodios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterEpisodio(getContext());
        rv_episodios.setAdapter(adapter);
    }
}