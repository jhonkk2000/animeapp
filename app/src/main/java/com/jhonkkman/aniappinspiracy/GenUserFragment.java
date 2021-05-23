package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GenUserFragment extends Fragment {

    private RecyclerView rv_gen;
    private LinearLayoutManager lym;
    private AdapterGen adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_user, container, false);
        rv_gen = view.findViewById(R.id.rv_gen_user);
        loadGen();

        return view;
    }
    public void loadGen(){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterGen();
        rv_gen.setLayoutManager(lym);
        rv_gen.setAdapter(adapter);
    }
}