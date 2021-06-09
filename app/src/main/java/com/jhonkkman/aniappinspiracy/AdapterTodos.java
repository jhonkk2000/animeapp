package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

public class AdapterTodos extends RecyclerView.Adapter<AdapterTodos.ViewHolderTodos> {

    private Context context;
    private List<AnimeItem> animes;
    private Activity activity;

    public AdapterTodos(Context context, List<AnimeItem> animes,Activity activity) {
        this.context = context;
        this.animes = animes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterTodos.ViewHolderTodos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTodos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTodos.ViewHolderTodos holder, int position) {
        holder.loadData(animes.get(position),position+1,context,activity);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public static class ViewHolderTodos extends RecyclerView.ViewHolder{

        ImageView iv_anime;
        MaterialCardView cv_anime;
        TextView tv_anime, tv_score, tv_position;

        public ViewHolderTodos(@NonNull View v) {
            super(v);
            iv_anime = v.findViewById(R.id.iv_anime_top);
            cv_anime = v.findViewById(R.id.cv_anime_top);
            tv_anime = v.findViewById(R.id.tv_anime_top);
            tv_score = v.findViewById(R.id.tv_score_top);
            tv_position = v.findViewById(R.id.tv_position_top);
        }

        public void loadData(AnimeItem anime, int position, Context context,Activity activity){
            Glide.with(context).load(anime.getImage_url()).into(iv_anime);
            tv_anime.setText(anime.getTitle());
            tv_score.setText(String.valueOf(anime.getScore()));
            tv_position.setText(String.valueOf(position));
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
