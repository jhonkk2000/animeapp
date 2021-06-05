package com.jhonkkman.aniappinspiracy.ui.perfil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhonkkman.aniappinspiracy.AdapterGaleria;
import com.jhonkkman.aniappinspiracy.R;

public class PerfilFragment extends Fragment {

    private RecyclerView rv_change_img;
    private LinearLayoutManager lym;
    private AdapterGaleria adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        rv_change_img = view.findViewById(R.id.rv_change_img);
        //loadImg();

        return view;
    }

    public void loadImg(){
        lym = new LinearLayoutManager(getContext());
        //adapter = new AdapterResultados();
        rv_change_img.setLayoutManager(lym);
        rv_change_img.setAdapter(adapter);
    }
}