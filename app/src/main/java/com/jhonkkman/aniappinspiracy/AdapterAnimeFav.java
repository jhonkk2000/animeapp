package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterAnimeFav extends RecyclerView.Adapter<AdapterAnimeFav.ViewHolderAnimeFav> {

    private Context context;
    private Activity activity;
    private ArrayList<AnimeResource> animes;
    private String activityUp;

    public AdapterAnimeFav(Context context, Activity activity, ArrayList<AnimeResource> animes,String activityUp) {
        this.context = context;
        this.activity = activity;
        this.animes = animes;
        this.activityUp = activityUp;
    }

    @NonNull
    @Override
    public AdapterAnimeFav.ViewHolderAnimeFav onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAnimeFav(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_fav,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAnimeFav.ViewHolderAnimeFav holder, int position) {
        holder.loadData(context,activity,animes.get(position),activityUp);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public static class ViewHolderAnimeFav extends RecyclerView.ViewHolder{

        ImageView iv_anime;
        MaterialCardView cv_anime;
        TextView tv_titulo,tv_synopsis,tv_emision,tv_fecha,tv_score,tv_type,tv_mejor,tv_mas;

        public ViewHolderAnimeFav(@NonNull View v) {
            super(v);
            iv_anime = v.findViewById(R.id.iv_anime_fav);
            tv_mas = v.findViewById(R.id.tv_mas);
            tv_mejor = v.findViewById(R.id.tv_mejor);
            tv_titulo = v.findViewById(R.id.tv_anime_fav_user);
            tv_synopsis = v.findViewById(R.id.tv_synopsis_fav);
            tv_emision = v.findViewById(R.id.tv_emision_fav);
            tv_fecha = v.findViewById(R.id.tv_fecha_fav);
            tv_score = v.findViewById(R.id.tv_score_fav);
            tv_type = v.findViewById(R.id.tv_type_fav);
            cv_anime = v.findViewById(R.id.cv_anime_fav);
        }

        public void loadData(Context context, Activity activity, AnimeResource anime,String acitivityUp){
            if(acitivityUp.equals("Found")){
                tv_mejor.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv_mejor.requestLayout();
                tv_mas.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv_mas.requestLayout();
            }else{
                tv_mejor.getLayoutParams().height = 0;
                tv_mejor.requestLayout();
                tv_mas.getLayoutParams().height = 0;
                tv_mas.requestLayout();

            }
            Glide.with(context).load(anime.getImage_url()).into(iv_anime);
            tv_titulo.setText(anime.getTitle());
            if(anime.isAiring()){
                tv_emision.setText("En emision");
            }else{
                tv_emision.setText("Finalizado");
            }
            //tv_fecha.setText("Fecha de estreno: " + anime.getAired().getProp().getFrom().getDay() + "/" + anime.getAired().getProp().getFrom().getMonth() + "/" + anime.getAired().getProp().getFrom().getYear());
            tv_type.setText(anime.getType());
            tv_score.setText(String.valueOf(anime.getScore()));
            TranslatorOptions options =
                    new TranslatorOptions.Builder()
                            .setSourceLanguage(TranslateLanguage.ENGLISH)
                            .setTargetLanguage(TranslateLanguage.SPANISH)
                            .build();
            final Translator translator =
                    Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @SuppressLint("NewApi")
                @Override
                public void onSuccess(Void unused) {
                    translator.translate(anime.getSynopsis()).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tv_synopsis.setText(s);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("TRADUCTOR","fallo");
                }
            });
            cv_anime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,AnimeActivity.class);
                    AnimeItem anime1 = new AnimeItem(anime.getMal_id(),anime.getTitle(),anime.getImage_url());
                    anime1.setUrl(anime.getUrl());
                    i.putExtra("anime", (Serializable) anime1);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_anime, ViewCompat.getTransitionName(cv_anime));
                    context.startActivity(i,options.toBundle());
                }
            });
        }
    }
}
