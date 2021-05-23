package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAni extends RecyclerView.Adapter<AdapterAni.ViewHolderAni> {

    private Context context;

    public AdapterAni(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterAni.ViewHolderAni onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAni(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ani_fav,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAni.ViewHolderAni holder, int position) {
        holder.selectAnime(context);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolderAni extends RecyclerView.ViewHolder{

        private CardView cv_anime1,cv_anime2;
        private boolean STATE_1 = false;
        private boolean STATE_2 = false;
        private ImageView iv_check1,iv_check2;

        public ViewHolderAni(@NonNull View v) {
            super(v);
            cv_anime1 = v.findViewById(R.id.cv_anime1);
            cv_anime2 = v.findViewById(R.id.cv_anime2);
            iv_check1 = v.findViewById(R.id.iv_check1);
            iv_check2 = v.findViewById(R.id.iv_check2);
        }

        public void selectAnime(Context context){
            cv_anime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(STATE_1){
                        cv_anime1.setForeground(null);
                        iv_check1.setAlpha(0.0f);
                        //animation(cv_anime1,R.color.green_transparency,Color.TRANSPARENT);
                        STATE_1 = false;
                    }else{
                        STATE_1 = true;
                        //cv_anime1.setForeground(context.getDrawable(R.color.green_transparency));
                        animation(cv_anime1,Color.TRANSPARENT,R.color.green_transparency,iv_check1);
                    }
                }
            });
            cv_anime2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(STATE_2){
                        cv_anime2.setForeground(null);
                        iv_check2.setAlpha(0.0f);
                        STATE_2 = false;
                    }else{
                        STATE_2 = true;
                        animation(cv_anime2,Color.TRANSPARENT,R.color.green_transparency,iv_check2);
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
