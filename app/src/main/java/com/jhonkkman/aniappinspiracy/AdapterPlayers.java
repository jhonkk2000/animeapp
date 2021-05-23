package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.ViewHolderPlayers> {
    @NonNull
    @Override
    public AdapterPlayers.ViewHolderPlayers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderPlayers(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayers.ViewHolderPlayers holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolderPlayers extends RecyclerView.ViewHolder{

        public ViewHolderPlayers(@NonNull View itemView) {
            super(itemView);
        }
    }
}
