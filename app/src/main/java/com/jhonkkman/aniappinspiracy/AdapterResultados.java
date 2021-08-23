package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterResultados extends RecyclerView.Adapter<AdapterResultados.ViewHolderResultados> {
    private ArrayList<AnimeItem> lista1 = new ArrayList<>();
    private ArrayList<AnimeItem> lista2 = new ArrayList<>();
    private ArrayList<AnimeItem> lista3 = new ArrayList<>();
    private Context context;
    private Activity activity;
    private String activityUp;

    public AdapterResultados(ArrayList<AnimeItem> lista1, ArrayList<AnimeItem> lista2, ArrayList<AnimeItem> lista3, Context context,Activity activity,String activityUp) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.lista3 = lista3;
        this.context = context;
        this.activity = activity;
        this.activityUp = activityUp;
    }

    @NonNull
    @Override
    public AdapterResultados.ViewHolderResultados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderResultados(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResultados.ViewHolderResultados holder, int position) {
        holder.loadData(lista1,lista2,lista3,position,context,activity,activityUp);
    }

    @Override
    public int getItemCount() {
        return lista1.size();
    }

    public static class ViewHolderResultados extends RecyclerView.ViewHolder{

        ImageView iv_1,iv_2,iv_3;
        MaterialCardView cv_1,cv_2,cv_3;

        public ViewHolderResultados(@NonNull View v) {
            super(v);
            iv_1 = v.findViewById(R.id.iv_resultado_1);
            iv_2 = v.findViewById(R.id.iv_resultado_2);
            iv_3 = v.findViewById(R.id.iv_resultado_3);
            cv_1 = v.findViewById(R.id.cv_resultado_1);
            cv_2 = v.findViewById(R.id.cv_resultado_2);
            cv_3 = v.findViewById(R.id.cv_resultado_3);
            cv_1.setTransitionName("anime_portada");
            cv_2.setTransitionName("anime_portada");
            cv_3.setTransitionName("anime_portada");
        }

        public void loadData(ArrayList<AnimeItem> lista1, ArrayList<AnimeItem> lista2, ArrayList<AnimeItem> lista3, int pos, Context context, Activity activity,String activityUp){
            if(activityUp.equals("inicio")){
                cv_1.setStrokeWidth(0);
                cv_2.setStrokeWidth(0);
                cv_3.setStrokeWidth(0);
            }
            if(lista1.size()>=pos+1){
                Glide.with(context).load(lista1.get(pos).getImage_url()).into(iv_1);
                cv_1.setVisibility(View.VISIBLE);
                cv_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context,AnimeActivity.class);
                        i.putExtra("anime", (Serializable) lista1.get(pos));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_1, ViewCompat.getTransitionName(cv_1));
                        context.startActivity(i,options.toBundle());
                    }
                });
            }else{
                cv_1.setVisibility(View.INVISIBLE);

            }
            if(lista2.size()>=pos+1){
                Glide.with(context).load(lista2.get(pos).getImage_url()).into(iv_2);
                cv_2.setVisibility(View.VISIBLE);
                cv_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context,AnimeActivity.class);
                        i.putExtra("anime", (Serializable) lista2.get(pos));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_2, ViewCompat.getTransitionName(cv_2));
                        context.startActivity(i,options.toBundle());
                    }
                });
            }else{
                cv_2.setVisibility(View.INVISIBLE);
                Log.d("Falla2",lista2.size()+"");
            }
            if(lista3.size()>=pos+1){
                Glide.with(context).load(lista3.get(pos).getImage_url()).into(iv_3);
                cv_3.setVisibility(View.VISIBLE);
                cv_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context,AnimeActivity.class);
                        i.putExtra("anime", (Serializable) lista3.get(pos));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_3, ViewCompat.getTransitionName(cv_3));
                        context.startActivity(i,options.toBundle());
                    }
                });
            }else{
                cv_3.setVisibility(View.INVISIBLE);
                Log.d("Falla3",lista3.size()+"");
            }

        }

    }
}
