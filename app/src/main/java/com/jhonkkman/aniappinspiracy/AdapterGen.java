package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGen extends RecyclerView.Adapter<AdapterGen.ViewHolderGen> {
    @NonNull
    @Override
    public AdapterGen.ViewHolderGen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderGen(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gen_fav,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGen.ViewHolderGen holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public static class ViewHolderGen extends RecyclerView.ViewHolder{

        public ViewHolderGen(@NonNull View itemView) {
            super(itemView);
        }
    }
}
