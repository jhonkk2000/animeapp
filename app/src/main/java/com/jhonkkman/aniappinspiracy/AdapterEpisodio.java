package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;
import com.jhonkkman.aniappinspiracy.data.models.Episodio;

import java.io.IOException;
import java.util.ArrayList;

import static com.jhonkkman.aniappinspiracy.AnimeActivity.anime_previous;

public class AdapterEpisodio extends RecyclerView.Adapter<AdapterEpisodio.ViewHolderEpisodio> {

    Context context;
    private int episodios;

    public AdapterEpisodio(Context context, int episodios) {
        this.context = context;
        this.episodios = episodios;
    }

    @NonNull
    @Override
    public ViewHolderEpisodio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderEpisodio(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodio, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEpisodio holder, int position) {
        holder.loadPlayers(context,position+1);
        holder.loadText(position + 1);
    }


    @Override
    public int getItemCount() {
        return episodios;
    }

    public static class ViewHolderEpisodio extends RecyclerView.ViewHolder {

        ImageButton btn_enter;
        TextView tv_episodio;

        public ViewHolderEpisodio(@NonNull View v) {
            super(v);
            btn_enter = v.findViewById(R.id.btn_enter_episode);
            tv_episodio = v.findViewById(R.id.tv_episodio);
        }

        public void loadText(int position) {
            tv_episodio.setText("Episodio " + position);
        }

        public void loadPlayers(Context context,int position) {
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnimeActivity.dialog.showDialog((Activity) context, "Cargando reproductores");
                    ArrayList<String> videos = loadEpisode(position);
                    if(videos.size()!=0){
                        Intent i = new Intent(context, PlayersActivity.class);
                        i.putExtra("videos", videos);
                        context.startActivity(i);
                    }else{
                        AnimeActivity.dialog.dismissDialog();
                        Toast.makeText(context, "Episodio no disponible.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public ArrayList<String> loadEpisode(int episode) {
            String[] anime_name = anime_previous.getUrl().split("/");
            ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1], episode);
            ArrayList<String> videos = new ArrayList<>();
            try {
                videos = apiVideoServer.getVideoServers();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return videos;
        }
    }
}
