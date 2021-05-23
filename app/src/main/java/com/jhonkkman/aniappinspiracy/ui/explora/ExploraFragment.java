package com.jhonkkman.aniappinspiracy.ui.explora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AnimeActivity;
import com.jhonkkman.aniappinspiracy.MainActivity;
import com.jhonkkman.aniappinspiracy.R;

public class ExploraFragment extends Fragment {

    private RecyclerView rv_resultados;
    private LinearLayoutManager lym;
    private AdapterResultados adapter;
    private MaterialCardView cv_best_anime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explora, container, false);
        rv_resultados = root.findViewById(R.id.rv_mas_resultados);
        cv_best_anime = root.findViewById(R.id.cv_best_anime);
        loadResultados();
        loadAnime();
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

    public void loadAnime(){
        cv_best_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), cv_best_anime, ViewCompat.getTransitionName(cv_best_anime));
                startActivity(new Intent(getContext(), AnimeActivity.class),options.toBundle());
            }
        });
    }
}