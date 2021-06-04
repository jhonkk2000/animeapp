package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;

import java.util.List;

public class AdapterAni extends RecyclerView.Adapter<AdapterAni.ViewHolderAni> {

    private Context context;
    private List<AnimeItem> list;
    private int[][] items = new int[25][2];
    private int[][] anime_selected = new int[25][2];

    public AdapterAni(Context context, List<AnimeItem> list){
        this.context = context;
        this.list = list;
        for (int i = 0; i < 25; i++) {
            items[i][0]=0;
            items[i][1]=0;
        }
    }

    public int[][] getAnime_selected() {
        return anime_selected;
    }

    @NonNull
    @Override
    public AdapterAni.ViewHolderAni onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAni(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ani_fav,null,false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterAni.ViewHolderAni holder, int position) {
        //holder.selectAnime(context,list.get(position),list.get(list.size()-1-position));
        holder.load(context,list.get(position),list.get(list.size()-1-position));
        int valor = 0;
        for (int i = 0; i < 25; i++) {
            if(i==position){
                valor = i;
                if(items[i][0]==0){
                    holder.cv_anime1.setForeground(null);
                    holder.iv_check1.setAlpha(0.0f);
                }else{
                    holder.cv_anime1.setForeground(new ColorDrawable(R.color.green_transparency));
                    holder.iv_check1.setAlpha(1.0f);
                }
                if(items[i][1]==0){
                    holder.cv_anime2.setForeground(null);
                    holder.iv_check2.setAlpha(0.0f);
                }else{
                    holder.cv_anime2.setForeground(new ColorDrawable(R.color.green_transparency));
                    holder.iv_check2.setAlpha(1.0f);
                }
            }
        }
        int finalValor = valor;
        holder.cv_anime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_anime1.setClickable(false);
                if(items[finalValor][0]==1){
                    holder.cv_anime1.setForeground(null);
                    holder.iv_check1.setAlpha(0.0f);
                    items[finalValor][0]=0;
                    anime_selected[finalValor][0] = 0;
                    holder.cv_anime1.setClickable(true);
                }else{
                    items[finalValor][0]=1;
                    anime_selected[finalValor][0] = list.get(position).getMal_id();
                    holder.animation(holder.cv_anime1,Color.TRANSPARENT,R.color.green_transparency,holder.iv_check1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.cv_anime1.setClickable(true);
                        }
                    },200);
                }
            }
        });
        holder.cv_anime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_anime2.setClickable(false);
                if(items[finalValor][1]==1){
                    holder.cv_anime2.setForeground(null);
                    holder.iv_check2.setAlpha(0.0f);
                    items[finalValor][1]=0;
                    anime_selected[finalValor][1] = 0;
                    holder.cv_anime2.setClickable(true);
                }else{
                    items[finalValor][1]=1;
                    anime_selected[finalValor][1] = list.get(list.size()-1-position).getMal_id();
                    holder.animation(holder.cv_anime2,Color.TRANSPARENT,R.color.green_transparency,holder.iv_check2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.cv_anime2.setClickable(true);
                        }
                    },200);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public static class ViewHolderAni extends RecyclerView.ViewHolder{

        private CardView cv_anime1,cv_anime2;
        private boolean STATE_1 = false;
        private boolean STATE_2 = false;
        private ImageView iv_check1,iv_check2,iv_anime1,iv_anime2;
        private TextView tv_anime1,tv_anime2;

        public ViewHolderAni(@NonNull View v) {
            super(v);
            cv_anime1 = v.findViewById(R.id.cv_anime1);
            cv_anime2 = v.findViewById(R.id.cv_anime2);
            iv_check1 = v.findViewById(R.id.iv_check1);
            iv_check2 = v.findViewById(R.id.iv_check2);
            iv_anime1 = v.findViewById(R.id.iv_anime_top1);
            iv_anime2 = v.findViewById(R.id.iv_anime_top2);
            tv_anime1 = v.findViewById(R.id.tv_anime_fav_1);
            tv_anime2 = v.findViewById(R.id.tv_anime_fav_2);
        }

        public void load(Context context, AnimeItem anime1, AnimeItem anime2){
            Glide.with(context).load(anime1.getImage_url()).into(iv_anime1);
            Glide.with(context).load(anime2.getImage_url()).into(iv_anime2);
            tv_anime1.setText(anime1.getTitle());
            tv_anime2.setText(anime2.getTitle());
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
        public void selectAnime(Context context, AnimeItem anime1, AnimeItem anime2){
            Glide.with(context).load(anime1.getImage_url()).into(iv_anime1);
            Glide.with(context).load(anime2.getImage_url()).into(iv_anime2);
            tv_anime1.setText(anime1.getTitle());
            tv_anime2.setText(anime2.getTitle());
            cv_anime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cv_anime1.setClickable(false);
                    if(STATE_1){
                        cv_anime1.setForeground(null);
                        iv_check1.setAlpha(0.0f);
                        STATE_1 = false;
                        cv_anime1.setClickable(true);
                    }else{
                        STATE_1 = true;
                        animation(cv_anime1,Color.TRANSPARENT,R.color.green_transparency,iv_check1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cv_anime1.setClickable(true);
                            }
                        },200);
                    }
                }
            });
            cv_anime2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cv_anime2.setClickable(false);
                    if(STATE_2){
                        cv_anime2.setForeground(null);
                        iv_check2.setAlpha(0.0f);
                        STATE_2 = false;
                        cv_anime2.setClickable(true);
                    }else{
                        STATE_2= true;
                        animation(cv_anime2,Color.TRANSPARENT,R.color.green_transparency,iv_check2);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cv_anime2.setClickable(true);
                            }
                        },200);
                    }
                }
            });
        }

        public void animation(CardView cv,int color1,int color2,ImageView iv){
            ColorDrawable[] colorDrawables = {new ColorDrawable(color1),
                    new ColorDrawable(color2)};
            TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
            cv.setForeground(transitionDrawable);
            transitionDrawable.startTransition(200);
            iv.animate()
                    .alpha(1.0f)
                    .setDuration(200);
        }
    }
}
