package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ComentariosFragment extends Fragment {

    private RecyclerView rv_c;
    private LinearLayoutManager lym;
    private AdapterComentario adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);
        rv_c = view.findViewById(R.id.rv_comentarios);

        loadComentarios();
        return view;
    }

    public void loadComentarios(){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterComentario();
        rv_c.setLayoutManager(lym);
        rv_c.setAdapter(adapter);
    }
}