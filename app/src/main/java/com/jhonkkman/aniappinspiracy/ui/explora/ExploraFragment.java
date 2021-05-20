package com.jhonkkman.aniappinspiracy.ui.explora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.R;

public class ExploraFragment extends Fragment {

    private RecyclerView rv_resultados;
    private LinearLayoutManager lym;
    private AdapterResultados adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explora, container, false);
        rv_resultados = root.findViewById(R.id.rv_mas_resultados);
        loadResultados();
        return root;
    }

    public void loadResultados(){
        lym = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new AdapterResultados();
        rv_resultados.setLayoutManager(lym);
        rv_resultados.setAdapter(adapter);
    }
}