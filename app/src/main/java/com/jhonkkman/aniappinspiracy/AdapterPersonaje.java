package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class AdapterPersonaje extends RecyclerView.Adapter<AdapterPersonaje.ViewHolderPersonaje> {
    @NonNull
    @NotNull
    @Override
    public ViewHolderPersonaje onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderPersonaje(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personaje,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderPersonaje holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderPersonaje extends RecyclerView.ViewHolder{

        public ViewHolderPersonaje(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
