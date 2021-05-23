package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.ViewHolderComentario> {
    @NonNull
    @NotNull
    @Override
    public AdapterComentario.ViewHolderComentario onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderComentario(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentario.ViewHolderComentario holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderComentario extends RecyclerView.ViewHolder{

        public ViewHolderComentario(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
