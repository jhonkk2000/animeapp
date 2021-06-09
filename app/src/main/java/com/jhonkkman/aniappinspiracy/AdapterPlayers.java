package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.ViewHolderPlayers> {

    private Context context;
    private ArrayList<String> videos;

    public AdapterPlayers(Context context, ArrayList<String> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public AdapterPlayers.ViewHolderPlayers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderPlayers(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayers.ViewHolderPlayers holder, int position) {
        holder.loadPlayer(context,videos.get(position),position+1);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolderPlayers extends RecyclerView.ViewHolder{

        TextView tv_reproductor;
        ImageButton btn_reproducir;

        public ViewHolderPlayers(@NonNull View v) {
            super(v);
            tv_reproductor = v.findViewById(R.id.tv_reproductor);
            btn_reproducir = v.findViewById(R.id.btn_reproductor);
        }

        public void loadPlayer(Context context,String video,int pos){
            tv_reproductor.setText("Reproductor " + pos);
            btn_reproducir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(video), "video/*");
                    context.startActivity(Intent.createChooser(intent, "Reproducir video usando..."));
                }
            });
        }
    }
}
