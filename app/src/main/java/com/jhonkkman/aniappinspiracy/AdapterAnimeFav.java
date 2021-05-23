package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAnimeFav extends RecyclerView.Adapter<AdapterAnimeFav.ViewHolderAnimeFav> {
    @NonNull
    @Override
    public AdapterAnimeFav.ViewHolderAnimeFav onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAnimeFav(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_fav,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAnimeFav.ViewHolderAnimeFav holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderAnimeFav extends RecyclerView.ViewHolder{

        public ViewHolderAnimeFav(@NonNull View itemView) {
            super(itemView);
        }
    }
}
