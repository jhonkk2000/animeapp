package com.jhonkkman.aniappinspiracy;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.ViewHolderPlayers> {

    private Context context;
    private ArrayList<String> videos;
    private DatabaseReference dbr;
    private String key;
    private ArrayList<String> type;

    public AdapterPlayers(Context context, ArrayList<String> videos, DatabaseReference dbr, String key, ArrayList<String> type) {
        this.context = context;
        this.videos = videos;
        this.dbr = dbr;
        this.key = key;
        this.type = type;
    }

    @NonNull
    @Override
    public AdapterPlayers.ViewHolderPlayers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderPlayers(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayers.ViewHolderPlayers holder, int position) {
        holder.loadPlayer(context, videos.get(position), position + 1, dbr, key, type.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolderPlayers extends RecyclerView.ViewHolder {

        TextView tv_reproductor;
        ImageButton btn_reproducir;
        TemplateView templateView;

        public ViewHolderPlayers(@NonNull View v) {
            super(v);
            tv_reproductor = v.findViewById(R.id.tv_reproductor);
            btn_reproducir = v.findViewById(R.id.btn_reproductor);
        }

        public void loadPlayer(Context context, String video, int pos, DatabaseReference dbr, String key, String type) {
            if (type.equals("primary")) {
                tv_reproductor.setText("Reproductor primario ");
                /*btn_download.setEnabled(true);
                btn_download.setVisibility(View.VISIBLE);
                btn_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle("Descargar");
                        request.setDescription("Descargando...");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"" + System.currentTimeMillis());
                        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    }
                });*/
            } else {
                //btn_download.setEnabled(false);
                //btn_download.setVisibility(View.INVISIBLE);
                if(type.equals("lat")){
                    tv_reproductor.setText("Reproductor Latino");
                }else{
                    tv_reproductor.setText("Reproductor secundario");
                }
            }
            btn_reproducir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if(CenterActivity.login){
                        dbr.child(key).child("last_anime_view").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                ArrayList<Integer> ids = new ArrayList();
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    ids.add(Integer.parseInt(ds.getValue().toString()));
                                }
                                if(ids.size()==4){
                                    ids.remove(0);
                                }
                                for (int i = 0; i < ids.size(); i++) {
                                    if(ids.get(i)==AnimeActivity.anime_previous.getMal_id()){
                                        ids.remove(i);
                                        break;
                                    }
                                }
                                ids.add(AnimeActivity.anime_previous.getMal_id());
                                dbr.child(key).child("last_anime_view").setValue(ids);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }*/
                    if (type.equals("primary")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(video), "video/*");
                        context.startActivity(Intent.createChooser(intent, "Reproducir video usando..."));
                    } else {
                        Intent i = new Intent(context, WebPlayerActivity.class);
                        //Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(video));
                        i.putExtra("url", video);
                        context.startActivity(i);
                    }

                }
            });
        }
    }
}
