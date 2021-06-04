package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;

import java.io.Serializable;
import java.util.List;

public class AdapterSeasonAnime extends RecyclerView.Adapter<AdapterSeasonAnime.ViewHolderSeasonAnime> {

    private List<AnimeItem> lista;
    private Context context;
    private Activity activity;

    public AdapterSeasonAnime(Context context,List<AnimeItem> lista,Activity activity){
        this.context = context;
        this.lista = lista;
        this.activity=activity;
    }

    @NonNull
    @Override
    public AdapterSeasonAnime.ViewHolderSeasonAnime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderSeasonAnime(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_with_name,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSeasonAnime.ViewHolderSeasonAnime holder, int position) {
        holder.loadData(lista.get(position),context);
        holder.onClickAnime(lista.get(position),context,activity);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderSeasonAnime extends RecyclerView.ViewHolder{

        TextView tv_nombre;
        ImageView iv_anime;
        MaterialCardView cv_anime;

        public ViewHolderSeasonAnime(@NonNull View v) {
            super(v);
            tv_nombre = v.findViewById(R.id.tv_anime_season_name);
            iv_anime = v.findViewById(R.id.iv_anime_season);
            cv_anime = v.findViewById(R.id.cv_anime_season);
        }

        public void onClickAnime(AnimeItem anime,Context context,Activity activity){
            cv_anime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,AnimeActivity.class);
                    i.putExtra("anime", (Serializable) anime);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_anime, ViewCompat.getTransitionName(cv_anime));
                    context.startActivity(i,options.toBundle());
                }
            });
        }

        public void loadData(AnimeItem animeItem, Context context){
            Glide.with(context).load(animeItem.getImage_url()).into(iv_anime);
            tv_nombre.setText(animeItem.getTitle());
        }
    }
}
