package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItemSearch;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.jhonkkman.aniappinspiracy.ui.inicio.InicioFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterInicioRv extends RecyclerView.Adapter<AdapterInicioRv.ViewHolderInicioRv> {

    private Context context;
    private List<GeneroItem> generos;
    private Activity activity;
    private boolean lastAnimeView;
    private ArrayList<AnimeItem> animes;
    private ArrayList<ArrayList<AnimeItem>> animesG;

    public AdapterInicioRv(Context context,List<GeneroItem> generos,Activity activity,boolean lastAnimeView,ArrayList<AnimeItem> animes,ArrayList<ArrayList<AnimeItem>> animesG){
        this.context = context;
        this.generos = generos;
        this.activity = activity;
        this.lastAnimeView = lastAnimeView;
        this.animes = animes;
        this.animesG = animesG;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterInicioRv.ViewHolderInicioRv onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderInicioRv(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_inicio,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterInicioRv.ViewHolderInicioRv holder, int position) {
        if(lastAnimeView){
            if(position==0){
                holder.loadLast(context,animes,activity);
            }else{
                holder.loadData(context,generos.get(position-1),activity,animesG.get(position-1));
            }
        }else{
            holder.loadData(context,generos.get(position),activity,animesG.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (lastAnimeView){
            if(animes.size()!=0){
                return animesG.size()+1;
            }else{
                return 0;
            }
        }else{
            return generos.size();
        }
    }

    public static class ViewHolderInicioRv extends RecyclerView.ViewHolder{

        RecyclerView rv_anime;
        LinearLayoutManager lym;
        AdapterAnimeImage adapter;
        TextView tv_nombre;

        public ViewHolderInicioRv(@NonNull @NotNull View v) {
            super(v);
            rv_anime = v.findViewById(R.id.rv_continue);
            tv_nombre = v.findViewById(R.id.tv_nombre_item_inicio);
        }


        public void loadData(Context context,GeneroItem genero,Activity activity,ArrayList<AnimeItem> animes){
            lym = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            adapter = new AdapterAnimeImage(context,animes,activity);
            rv_anime.setLayoutManager(lym);
            rv_anime.setAdapter(adapter);
            rv_anime.scheduleLayoutAnimation();
            tv_nombre.setText(genero.getName());
        }

        public void loadLast(Context context, ArrayList<AnimeItem> animes, Activity activity){
                lym = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                adapter = new AdapterAnimeImage(context,animes,activity);
                rv_anime.setLayoutManager(lym);
                rv_anime.setAdapter(adapter);
                rv_anime.scheduleLayoutAnimation();
                tv_nombre.setText("Ultimos animes vistos");
                reload(adapter);
        }

        public void reload(AdapterAnimeImage adapter){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(InicioFragment.estado_last){
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter.notifyDataSetChanged();
                        reload(adapter);
                    }
                }
            },1000);
        }

    }
}
