package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PersonajesFragment extends Fragment {

    private RecyclerView rv_p;
    private LinearLayoutManager lym;
    private AdapterPersonaje adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personajes, container, false);
        rv_p = view.findViewById(R.id.rv_personajes);
        loadPersonajes();
        return view;
    }

    public void loadPersonajes(){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterPersonaje();
        rv_p.setLayoutManager(lym);
        rv_p.setAdapter(adapter);
    }
}