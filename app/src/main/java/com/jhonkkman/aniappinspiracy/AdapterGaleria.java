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
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import java.util.ArrayList;

public class AdapterGaleria extends RecyclerView.Adapter<AdapterGaleria.ViewHolderGaleria> {

    private ArrayList<Picture> lista1 = new ArrayList<>();
    private ArrayList<Picture> lista2 = new ArrayList<>();
    private ArrayList<Picture> lista3 = new ArrayList<>();
    private Context context;
    private Activity activity;

    public AdapterGaleria(ArrayList<Picture> lista1, ArrayList<Picture> lista2, ArrayList<Picture> lista3, Context context,Activity activity) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.lista3 = lista3;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolderGaleria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderGaleria(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGaleria holder, int position) {
        holder.loadData(lista1,lista2,lista3,position,context,activity);
    }

    @Override
    public int getItemCount() {
        return lista1.size();
    }

    public static class ViewHolderGaleria extends RecyclerView.ViewHolder{

        ImageView iv_1,iv_2,iv_3;
        MaterialCardView cv_1,cv_2,cv_3;
        TemplateView templateView;

        public ViewHolderGaleria(@NonNull View v) {
            super(v);
            iv_1 = v.findViewById(R.id.iv_resultado_1);
            iv_2 = v.findViewById(R.id.iv_resultado_2);
            iv_3 = v.findViewById(R.id.iv_resultado_3);
            cv_1 = v.findViewById(R.id.cv_resultado_1);
            cv_2 = v.findViewById(R.id.cv_resultado_2);
            cv_3 = v.findViewById(R.id.cv_resultado_3);
            templateView = v.findViewById(R.id.templateview_resultados);
        }

        public void loadData(ArrayList<Picture> lista1,ArrayList<Picture> lista2,ArrayList<Picture> lista3,int pos,Context context,Activity activity){
            templateView.getLayoutParams().height = 0;
            templateView.requestLayout();
            if(lista1.size()>=pos+1){
                Glide.with(context).load(lista1.get(pos).getSmall()).into(iv_1);
                openImage(cv_1,context,lista1.get(pos).getLarge(),activity);
            }else{
                cv_1.setVisibility(View.INVISIBLE);
            }
            if(lista2.size()>=pos+1){
                Glide.with(context).load(lista2.get(pos).getSmall()).into(iv_2);
                openImage(cv_2,context,lista2.get(pos).getLarge(),activity);
            }else{
                cv_2.setVisibility(View.INVISIBLE);
            }
            if(lista3.size()>=pos+1){
                Glide.with(context).load(lista3.get(pos).getSmall()).into(iv_3);
                openImage(cv_3,context,lista3.get(pos).getLarge(),activity);
            }else{
                cv_3.setVisibility(View.INVISIBLE);
            }

        }

        public void openImage(MaterialCardView cv,Context context,String url,Activity activity){
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,ImageActivity.class);
                    i.putExtra("url",url);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv, ViewCompat.getTransitionName(cv));
                    context.startActivity(i,options.toBundle());
                }
            });
        }

    }
}
