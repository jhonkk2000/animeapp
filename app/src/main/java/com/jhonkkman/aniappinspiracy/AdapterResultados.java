package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import java.util.ArrayList;

public class AdapterResultados extends RecyclerView.Adapter<AdapterResultados.ViewHolderResultados> {

    private ArrayList<Picture> lista1 = new ArrayList<>();
    private ArrayList<Picture> lista2 = new ArrayList<>();
    private ArrayList<Picture> lista3 = new ArrayList<>();
    private Context context;

    public AdapterResultados(ArrayList<Picture> lista1, ArrayList<Picture> lista2, ArrayList<Picture> lista3, Context context) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.lista3 = lista3;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterResultados.ViewHolderResultados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderResultados(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResultados.ViewHolderResultados holder, int position) {
        holder.loadData(lista1,lista2,lista3,position,context);
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
        }

        public void loadData(ArrayList<Picture> lista1,ArrayList<Picture> lista2,ArrayList<Picture> lista3,int pos,Context context){
            if(lista1.size()>=pos+1){
                Glide.with(context).load(lista1.get(pos).getSmall()).into(iv_1);
            }else{
                cv_1.setVisibility(View.INVISIBLE);
            }
            if(lista2.size()>=pos+1){
                Glide.with(context).load(lista2.get(pos).getSmall()).into(iv_2);
            }else{
                cv_2.setVisibility(View.INVISIBLE);
            }
            if(lista3.size()>=pos+1){
                Glide.with(context).load(lista3.get(pos).getSmall()).into(iv_3);
            }else{
                cv_3.setVisibility(View.INVISIBLE);
            }

        }

    }
}
