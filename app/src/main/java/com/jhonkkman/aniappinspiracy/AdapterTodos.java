package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTodos extends RecyclerView.Adapter<AdapterTodos.ViewHolderTodos> {
    @NonNull
    @Override
    public AdapterTodos.ViewHolderTodos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTodos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTodos.ViewHolderTodos holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolderTodos extends RecyclerView.ViewHolder{

        public ViewHolderTodos(@NonNull View itemView) {
            super(itemView);
        }
    }
}
