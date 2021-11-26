package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jhonkkman.aniappinspiracy.data.models.Character;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterPersonaje extends RecyclerView.Adapter<AdapterPersonaje.ViewHolderPersonaje> {

    private Context context;
    private ArrayList<Character> characters;

    public AdapterPersonaje(Context context, ArrayList<Character> characters){
        this.context = context;
        this.characters = characters;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolderPersonaje onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderPersonaje(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personaje,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderPersonaje holder, int position) {
        holder.loadData(context,characters.get(position));
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class ViewHolderPersonaje extends RecyclerView.ViewHolder{

        TextView tv_anime,tv_actor;
        ImageView iv_anime,iv_actor;

        public ViewHolderPersonaje(@NonNull @NotNull View v) {
            super(v);
            iv_actor = v.findViewById(R.id.iv_actor_personaje);
            iv_anime = v.findViewById(R.id.iv_anime_personaje);
            tv_actor = v.findViewById(R.id.tv_actor_personaje);
            tv_anime = v.findViewById(R.id.tv_anime_personaje);
        }

        public void loadData(Context context,Character character){
            RequestOptions myOptions = new RequestOptions()
                    .override(200, 350);
            Glide.with(context).asBitmap().apply(myOptions).load(character.getImage_url()).into(iv_anime);
            tv_anime.setText(character.getName());
            if(character.getVoice_actors().size()!=0){
                tv_actor.setText(character.getVoice_actors().get(0).getName());
                Glide.with(context).asBitmap().apply(myOptions).load(character.getVoice_actors().get(0).getImage_url()).into(iv_actor);
            }else{
                tv_actor.setText("Aun no disponible");
                Glide.with(context).asBitmap().apply(myOptions).load(AnimeActivity.anime_previous.getImage_url()).into(iv_actor);
            }
        }
    }
}
