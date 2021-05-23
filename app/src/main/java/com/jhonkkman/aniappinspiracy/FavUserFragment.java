package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavUserFragment extends Fragment {

    private RecyclerView rv_fav;
    private LinearLayoutManager lym;
    private AdapterAnimeFav adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_user, container, false);
        rv_fav = view.findViewById(R.id.rv_fav_user);
        loadFavs();
        return view;
    }

    public void loadFavs(){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterAnimeFav();
        rv_fav.setLayoutManager(lym);
        rv_fav.setAdapter(adapter);
    }
}