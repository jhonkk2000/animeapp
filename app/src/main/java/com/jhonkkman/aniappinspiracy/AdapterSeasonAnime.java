package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSeasonAnime extends RecyclerView.Adapter<AdapterSeasonAnime.ViewHolderSeasonAnime> {

    @NonNull
    @Override
    public AdapterSeasonAnime.ViewHolderSeasonAnime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderSeasonAnime(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_with_name,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSeasonAnime.ViewHolderSeasonAnime holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderSeasonAnime extends RecyclerView.ViewHolder{

        public ViewHolderSeasonAnime(@NonNull View itemView) {
            super(itemView);
        }
    }
}
