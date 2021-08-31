package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;
import com.jhonkkman.aniappinspiracy.data.models.ItemSearch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolderSearch> {

    private ArrayList<ItemSearch> itemSearches;
    private int type;
    private Context context;
    private ApiAnimeData API_SERVICE;
    private String t = "";
    private Activity activity;

    public AdapterSearch(ArrayList<ItemSearch> itemSearches, int type, Context context) {
        this.itemSearches = itemSearches;
        this.type = type;
        this.context = context;
    }

    public void setAPI_SERVICE(ApiAnimeData API_SERVICE) {
        this.API_SERVICE = API_SERVICE;
    }

    public void setT(String t) {
        this.t = t;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterSearch.ViewHolderSearch onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderSearch(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterSearch.ViewHolderSearch holder, int position) {
        if (type == 0) {
            holder.loadDataExplora(itemSearches.get(position), context);
        } else {
            holder.loadDataWithImage(itemSearches.get(position), context, t, API_SERVICE, activity);
        }
    }

    @Override
    public int getItemCount() {
        return itemSearches.size();
    }

    public static class ViewHolderSearch extends RecyclerView.ViewHolder {

        TextView tv_nombre;
        MaterialCardView cv_search_item;
        ImageView iv_image;
        AlertLoading dialog = new AlertLoading();

        public ViewHolderSearch(@NonNull @NotNull View v) {
            super(v);
            tv_nombre = v.findViewById(R.id.tv_nombre_search);
            cv_search_item = v.findViewById(R.id.cv_search_item);
            iv_image = v.findViewById(R.id.iv_image_search_item);
        }

        public void loadDataExplora(ItemSearch item, Context context) {
            tv_nombre.setText(item.getNombre());
            cv_search_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, SearchItemAtivity.class);
                    i.putExtra("type", tv_nombre.getText().toString());
                    context.startActivity(i);
                }
            });
        }

        public void loadDataWithImage(ItemSearch item, Context context, String type, ApiAnimeData API, Activity activity) {
            tv_nombre.setText(item.getNombre());
            Glide.with(context).load(item.getUrl_foto()).into(iv_image);
            cv_search_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.showDialog(activity, "Buscando...");
                    if (type.equals("Generos")) {
                        ApiClientData.cargarLlamada(5, String.valueOf(item.getMall_id()), API, context,activity,tv_nombre.getText().toString(),dialog,1);
                    }else{
                        int val;
                        if(type.equals("Letra")) {
                            val = 1;
                        }else{
                            if(type.equals("Categoria")){
                                val =3;
                            }else{
                                if(type.equals("Tipo")){
                                    val = 2;
                                }else{
                                    val = 4;
                                }
                            }
                        }
                        ApiClientData.cargarLlamada(val, tv_nombre.getText().toString(), API, context,activity,tv_nombre.getText().toString(),dialog,1);
                    }
                }
            });
        }

    }
}
