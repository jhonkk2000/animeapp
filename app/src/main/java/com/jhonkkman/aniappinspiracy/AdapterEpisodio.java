package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterEpisodio extends RecyclerView.Adapter<AdapterEpisodio.ViewHolderEpisodio> {

    Context context;

    public AdapterEpisodio(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderEpisodio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderEpisodio(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodio,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEpisodio holder, int position) {
        holder.loadPlayers(context);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public static class ViewHolderEpisodio extends RecyclerView.ViewHolder{

        ImageButton btn_enter;

        public ViewHolderEpisodio(@NonNull View v) {
            super(v);
            btn_enter = v.findViewById(R.id.btn_enter_episode);
        }

        public void loadPlayers(Context context){
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,PlayersActivity.class));
                }
            });
        }
    }
}
