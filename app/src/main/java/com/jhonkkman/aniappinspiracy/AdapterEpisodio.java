package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;
import com.jhonkkman.aniappinspiracy.data.models.Episodio;

import java.io.IOException;
import java.util.ArrayList;

import static com.jhonkkman.aniappinspiracy.AnimeActivity.anime_previous;
import static com.jhonkkman.aniappinspiracy.AnimeActivity.avaibleLat;

public class AdapterEpisodio extends RecyclerView.Adapter<AdapterEpisodio.ViewHolderEpisodio> {

    Context context;
    private int episodios;
    private String url;

    public AdapterEpisodio(Context context, int episodios,String url) {
        this.context = context;
        this.episodios = episodios;
        this.url = url;
    }

    @NonNull
    @Override
    public ViewHolderEpisodio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderEpisodio(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodio, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEpisodio holder, int position) {
        holder.loadPlayers(context, position + 1,url,getItemCount());
        holder.loadText(position + 1);
    }


    @Override
    public int getItemCount() {
        return episodios;
    }

    public static class ViewHolderEpisodio extends RecyclerView.ViewHolder {

        ImageButton btn_enter;
        TextView tv_episodio;
        int state = 0;
        ArrayList<String> videosLinks;

        public ViewHolderEpisodio(@NonNull View v) {
            super(v);
            btn_enter = v.findViewById(R.id.btn_enter_episode);
            tv_episodio = v.findViewById(R.id.tv_episodio);
        }

        public void loadText(int position) {
            tv_episodio.setText("Episodio " + position);
        }

        public void loadPlayers(Context context, int position,String url,int epC) {
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnimeActivity.dialog.showDialog((Activity) context, "Cargando reproductores");
                    final ArrayList<String>[] videos = new ArrayList[1];
                    videos[0] = new ArrayList<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            videos[0] = loadEpisode(position);
                            videosLinks = videos[0];
                        }
                    }).start();
                    loadActivity(context,position,url,epC);
                }
            });
        }

        public void loadActivity( Context context,int ep,String url,int epC) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (state == 1) {
                        if (videosLinks.size() != 0 || avaibleLat) {
                            Intent i = new Intent(context, PlayersActivity.class);
                            i.putExtra("videos", videosLinks);
                            i.putExtra("ep",ep);
                            i.putExtra("urlA",url);
                            i.putExtra("epC",epC);
                            context.startActivity(i);
                            if(AnimeActivity.dialog.state){
                                AnimeActivity.dialog.dismissDialog();
                            }
                        } else {
                            if(AnimeActivity.dialog.state){
                                AnimeActivity.dialog.dismissDialog();
                            }
                            AlertUpdate alertUpdate = new AlertUpdate();
                            alertUpdate.alert = "play";
                            alertUpdate.showDialog((Activity) context);
                            alertUpdate.setTitulo("No disponible");
                            alertUpdate.btn_ok.setText("Aceptar");
                            alertUpdate.btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertUpdate.dismissDialog();
                                }
                            });
                            alertUpdate.setDesc("Vaya!, no hemos encontrado este anime, pero puedes solicitarlo pulsando el boton SOLICITAR o a traves de nuestro servidor de discord, y se agregara lo mas pronto posible, puedes encontrar" +
                                    " nuestro discord en el apartado de comunidad");
                        }
                    } else {
                        loadActivity(context,ep,url,epC);
                    }
                }
            }, 1000);
        }

        public ArrayList<String> loadEpisode(int episode) {
            String[] anime_name = anime_previous.getUrl().split("/");
            ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1], episode);
            ArrayList<String> videos = new ArrayList<>();
            try {
                videos = apiVideoServer.getVideoServersSub();
                state = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return videos;
        }
    }
}
