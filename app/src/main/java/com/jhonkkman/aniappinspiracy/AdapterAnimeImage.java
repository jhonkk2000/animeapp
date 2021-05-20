package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAnimeImage extends RecyclerView.Adapter<AdapterAnimeImage.ViewHolderAnimeImage> {
    @NonNull
    @Override
    public AdapterAnimeImage.ViewHolderAnimeImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAnimeImage(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_image,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAnimeImage.ViewHolderAnimeImage holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderAnimeImage extends RecyclerView.ViewHolder{

        public ViewHolderAnimeImage(@NonNull View itemView) {
            super(itemView);
        }
    }
}
