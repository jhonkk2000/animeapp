package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.jhonkkman.aniappinspiracy.data.models.Comentario;
import com.jhonkkman.aniappinspiracy.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.ViewHolderComentario> {

    private Context context;
    private ArrayList<Comentario> comentarios;
    private ArrayList<User> users = new ArrayList<>();
    private DatabaseReference dbr;
    private String KEY;

    public AdapterComentario(Context context, ArrayList<Comentario> comentarios,ArrayList<User> users,DatabaseReference dbr,String KEY) {
        this.context = context;
        this.comentarios = comentarios;
        this.users = users;
        this.dbr = dbr;
        this.KEY = KEY;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterComentario.ViewHolderComentario onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderComentario(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterComentario.ViewHolderComentario holder, int position) {
        holder.loadData(comentarios.get(position),users,context,dbr,KEY);
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ViewHolderComentario extends RecyclerView.ViewHolder{

        ImageView iv_user;
        TextView tv_user,tv_comentario,tv_like,tv_dislike;
        LinearLayout btn_like,btn_dislike;

        public ViewHolderComentario(@NonNull @NotNull View v) {
            super(v);
            iv_user = v.findViewById(R.id.iv_user_comentario);
            tv_comentario = v.findViewById(R.id.tv_comentario);
            tv_user = v.findViewById(R.id.tv_nombreu_comentario);
            tv_like = v.findViewById(R.id.tv_like);
            tv_dislike = v.findViewById(R.id.tv_dislike);
            btn_like = v.findViewById(R.id.btn_like);
            btn_dislike = v.findViewById(R.id.btn_dislike);
        }

        public void loadData(Comentario comentario,ArrayList<User> users,Context context,DatabaseReference dbr,String KEY){
            if(!CenterActivity.login){
                btn_like.setEnabled(false);
                btn_dislike.setEnabled(false);
            }
            ArrayList<String> likes = comentario.getLikes();
            ArrayList<String> dislikes = comentario.getDislikes();
            User user = new User();
            for (int i = 0; i < users.size(); i++) {
                if(users.get(i).getCorreo().equals(comentario.getUser())){
                    user = users.get(i);
                    break;
                }
            }
            Glide.with(context).load(user.getUrl_foto()).into(iv_user);
            tv_comentario.setText(comentario.getComentario());
            tv_user.setText(user.getNombre_usuario());
            tv_like.setText(String.valueOf(comentario.getLikes().size()));
            tv_dislike.setText(String.valueOf(comentario.getDislikes().size()));
            boolean userLike = false;
            boolean userDislike = false;
            for (int i = 0; i < comentario.getLikes().size(); i++) {
                if(comentario.getLikes().get(i).equals(ComentariosFragment.user.getCorreo())){
                    userLike = true;
                }
            }
            for (int i = 0; i < comentario.getDislikes().size(); i++) {
                if(comentario.getDislikes().get(i).equals(ComentariosFragment.user.getCorreo())){
                    userDislike = true;
                }
            }
            if(userLike){
                btn_like.setBackground(context.getDrawable(R.drawable.background_like));
            }else{
                btn_like.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
            }
            if(userDislike){
                btn_dislike.setBackground(context.getDrawable(R.drawable.background_dislike));
            }else{
                btn_dislike.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
            }
            boolean finalUserLike = userLike;
            boolean finalUserDislike = userDislike;
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalUserLike){
                        likes.remove(ComentariosFragment.user.getCorreo());
                        dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("likes").setValue(likes);
                        btn_like.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
                    }else{
                        if(finalUserDislike){
                            dislikes.remove(ComentariosFragment.user.getCorreo());
                            dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("dislikes").setValue(dislikes);
                            btn_dislike.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
                        }
                        likes.add(ComentariosFragment.user.getCorreo());
                        Log.d("KEY AND NUM", "KEY: " + KEY + " NUM: " +comentario.getNum());
                        dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("likes").setValue(likes);
                        btn_like.setBackground(context.getDrawable(R.drawable.background_like));
                    }
                }
            });
            btn_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalUserDislike){
                        dislikes.remove(ComentariosFragment.user.getCorreo());
                        dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("dislikes").setValue(dislikes);
                        btn_dislike.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
                    }else{
                        if(finalUserLike){
                            likes.remove(ComentariosFragment.user.getCorreo());
                            dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("likes").setValue(likes);
                            btn_like.setBackground(context.getDrawable(R.drawable.backgroun_disable_like));
                        }
                        btn_dislike.setBackground(context.getDrawable(R.drawable.background_dislike));
                        dislikes.add(ComentariosFragment.user.getCorreo());
                        dbr.child("anime").child(KEY).child("comentarios").child(comentario.getNum()).child("dislikes").setValue(dislikes);
                    }
                }
            });
        }
    }
}
