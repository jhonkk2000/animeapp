package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterResultados extends RecyclerView.Adapter<AdapterResultados.ViewHolderResultados> {
    @NonNull
    @Override
    public AdapterResultados.ViewHolderResultados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderResultados(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResultados.ViewHolderResultados holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolderResultados extends RecyclerView.ViewHolder{

        public ViewHolderResultados(@NonNull View itemView) {
            super(itemView);
        }
    }
}
