package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhonkkman.aniappinspiracy.data.models.Episodio;

import java.util.ArrayList;

public class AdapterEpisodio extends RecyclerView.Adapter<AdapterEpisodio.ViewHolderEpisodio> {

    Context context;
    private ArrayList<Episodio> episodios;

    public AdapterEpisodio(Context context,ArrayList<Episodio> episodios){
        this.context = context;
        this.episodios = episodios;
    }

    @NonNull
    @Override
    public ViewHolderEpisodio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderEpisodio(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodio,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEpisodio holder, int position) {
        holder.loadPlayers(context,episodios.get(position).getVideos());
        holder.loadText(position+1);
    }



    @Override
    public int getItemCount() {
        return episodios.size();
    }

    public static class ViewHolderEpisodio extends RecyclerView.ViewHolder{

        ImageButton btn_enter;
        TextView tv_episodio;

        public ViewHolderEpisodio(@NonNull View v) {
            super(v);
            btn_enter = v.findViewById(R.id.btn_enter_episode);
            tv_episodio = v.findViewById(R.id.tv_episodio);
        }

        public void loadText(int position){
            tv_episodio.setText("Episodio " + position);
        }

        public void loadPlayers(Context context,ArrayList<String> videos){
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnimeActivity.dialog.showDialog((Activity) context,"Cargando reproductores");
                    Intent i = new Intent(context,PlayersActivity.class);
                    i.putExtra("videos",videos);
                    context.startActivity(i);
                }
            });
        }
    }
}
