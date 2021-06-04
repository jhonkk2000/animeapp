package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterAnimeImage extends RecyclerView.Adapter<AdapterAnimeImage.ViewHolderAnimeImage> {

    private Context context;
    private ArrayList<AnimeItem> animes;
    private Activity activity;

    public AdapterAnimeImage(Context context, ArrayList<AnimeItem> animes,Activity activity) {
        this.context = context;
        this.animes = animes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterAnimeImage.ViewHolderAnimeImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAnimeImage(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_image,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAnimeImage.ViewHolderAnimeImage holder, int position) {
        holder.loadData(animes.get(position),context,activity);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public static class ViewHolderAnimeImage extends RecyclerView.ViewHolder{

        ImageView iv_anime;
        MaterialCardView cv_anime;

        public ViewHolderAnimeImage(@NonNull View v) {
            super(v);
            iv_anime = v.findViewById(R.id.iv_anime_inicio);
            cv_anime = v.findViewById(R.id.cv_anime_inicio);
        }

        public void loadData(AnimeItem anime, Context context, Activity activity){
            Glide.with(context).load(anime.getImage_url()).into(iv_anime);
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
    }
}
