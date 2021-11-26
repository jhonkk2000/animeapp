package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import java.util.ArrayList;

public class AdapterGaleria extends RecyclerView.Adapter<AdapterGaleria.ViewHolderGaleria> {

    private ArrayList<Picture> lista1 = new ArrayList<>();
    private ArrayList<Picture> lista2 = new ArrayList<>();
    private ArrayList<String> lista1S = new ArrayList<>();
    private ArrayList<String> lista2S = new ArrayList<>();
    private ArrayList<String> imgs_perfil;
    private Context context;
    private Activity activity;
    private String activityUp;
    private DatabaseReference dbr;
    private String id_user;

    public AdapterGaleria(ArrayList<Picture> lista1, ArrayList<Picture> lista2, Context context,Activity activity,String activityUp) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.context = context;
        this.activity = activity;
        this.activityUp = activityUp;
    }
    public AdapterGaleria(ArrayList<String> lista1, ArrayList<String> lista2, Context context,String activityUp,DatabaseReference dbr,String id_user) {
        this.lista1S = lista1;
        this.lista2S = lista2;
        this.context = context;
        this.activityUp = activityUp;
        this.dbr = dbr;
        this.id_user = id_user;
    }

    @NonNull
    @Override
    public ViewHolderGaleria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderGaleria(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGaleria holder, int position) {
        if(activityUp.equals("galeria")){
            holder.loadData(lista1,lista2,position,context,activity);
        }else{
            holder.loadDataImgs(lista1S,lista2S,position,context,dbr,id_user);
        }
    }

    @Override
    public int getItemCount() {
        if(activityUp.equals("galeria")){
            return lista1.size();
        }else{
            return lista1S.size();
        }
    }

    public static class ViewHolderGaleria extends RecyclerView.ViewHolder{

        ImageView iv_1,iv_2;
        MaterialCardView cv_1,cv_2;

        public ViewHolderGaleria(@NonNull View v) {
            super(v);
            iv_1 = v.findViewById(R.id.iv_resultado_1);
            iv_2 = v.findViewById(R.id.iv_resultado_2);
            cv_1 = v.findViewById(R.id.cv_resultado_1);
            cv_2 = v.findViewById(R.id.cv_resultado_2);
        }

        public void loadDataImgs(ArrayList<String> lista1,ArrayList<String> lista2,int pos,Context context,DatabaseReference dbr,String id_user){
            if(lista1.size()>=pos+1){
                Glide.with(context).load(lista1.get(pos)).into(iv_1);
                setImage(cv_1,context,lista1.get(pos),dbr,id_user);
            }else{
                cv_1.setVisibility(View.INVISIBLE);
            }
            if(lista2.size()>=pos+1){
                Glide.with(context).load(lista2.get(pos)).into(iv_2);
                setImage(cv_2,context,lista2.get(pos),dbr,id_user);
            }else{
                cv_2.setVisibility(View.INVISIBLE);
            }
        }

        public void setImage(MaterialCardView cv,Context context,String url,DatabaseReference dbr,String id_user){
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertOptions alertOptions = new AlertOptions();
                    alertOptions.showDialog((Activity) context,"¿Deseas establecer esta imagen como nuevo foto de perfil?");
                    alertOptions.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(alertOptions.send){
                                dbr.child("users").child(id_user).child("url_foto").setValue(url);
                            }else{
                                alertOptions.dismissDialog();
                            }
                        }
                    });
                }
            });
        }

        public void loadData(ArrayList<Picture> lista1,ArrayList<Picture> lista2,int pos,Context context,Activity activity){
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
