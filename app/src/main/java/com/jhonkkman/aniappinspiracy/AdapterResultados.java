package com.jhonkkman.aniappinspiracy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterResultados extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<AnimeItem> lista1;
    private ArrayList<AnimeItem> lista2;
    private AnimeResource anime;
    private Context context;
    private Activity activity;
    private String activityUp;
    private final int BODY = 1;
    private final int HEADER = 2;

    public AdapterResultados(ArrayList<AnimeItem> lista1, ArrayList<AnimeItem> lista2, Context context, Activity activity, String activityUp) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.context = context;
        this.activity = activity;
        this.activityUp = activityUp;
    }

    public void setAnime(AnimeResource anime) {
        this.anime = anime;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER && activityUp.equals("Found")) {
            return new AdapterAnimeFav.ViewHolderAnimeFav(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_fav, null, false));
        } else {
            return new ViewHolderResultados(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados, null, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (activityUp.equals("Found")) {
            if (position == 0) {
                return HEADER;
            } else {
                return BODY;
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdapterAnimeFav.ViewHolderAnimeFav && activityUp.equals("Found")) {
            ((AdapterAnimeFav.ViewHolderAnimeFav) holder).loadData(context, activity, anime, activityUp);
        } else {
            if (holder instanceof ViewHolderResultados) {
                if (activityUp.equals("Found")) {
                    ((ViewHolderResultados) holder).loadData(lista1, lista2, position - 1, context, activity, activityUp, getItemCount());
                } else {
                    ((ViewHolderResultados) holder).loadData(lista1, lista2, position, context, activity, activityUp, getItemCount());
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (activityUp.equals("Found")) {
            return lista1.size() + 1;
        } else {
            return lista1.size();
        }
    }

    public static class ViewHolderResultados extends RecyclerView.ViewHolder {

        ImageView iv_1, iv_2;
        MaterialCardView cv_1, cv_2;
        MaterialButton btn_more;
        TextView tv_res1, tv_res2;

        public ViewHolderResultados(@NonNull View v) {
            super(v);
            iv_1 = v.findViewById(R.id.iv_resultado_1);
            iv_2 = v.findViewById(R.id.iv_resultado_2);
            cv_1 = v.findViewById(R.id.cv_resultado_1);
            cv_2 = v.findViewById(R.id.cv_resultado_2);
            tv_res1 = v.findViewById(R.id.tv_res1);
            tv_res2 = v.findViewById(R.id.tv_res2);
            btn_more = v.findViewById(R.id.btn_mas_resultados);
            cv_1.setTransitionName("anime_portada");
            cv_2.setTransitionName("anime_portada");
        }

        public void loadData(ArrayList<AnimeItem> lista1, ArrayList<AnimeItem> lista2, int pos, Context context, Activity activity, String activityUp, int itemC) {
            if (activityUp.equals("Found") && (pos + 2) == itemC) {
                Log.d("POSITION", "" + pos + " - " + itemC);
                btn_more.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                btn_more.requestLayout();
                btn_more.setEnabled(true);
                btn_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FoundAnimeActivity.loadMoreAnimes(activity);
                    }
                });
            } else {
                btn_more.getLayoutParams().height = 0;
                btn_more.setEnabled(false);
                btn_more.requestLayout();
            }
            RequestOptions myOptions = new RequestOptions()
                    .override(200, 350);
            if (lista1.size() >= pos + 1) {
                Glide.with(context).asBitmap().apply(myOptions).load(lista1.get(pos).getImage_url()).into(iv_1);
                tv_res1.setText(lista1.get(pos).getTitle());
                cv_1.setVisibility(View.VISIBLE);
                cv_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AnimeActivity.class);
                        i.putExtra("anime", (Serializable) lista1.get(pos));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_1, ViewCompat.getTransitionName(cv_1));
                        context.startActivity(i, options.toBundle());
                    }
                });
            } else {
                cv_1.setVisibility(View.INVISIBLE);
            }
            if (lista2.size() >= pos + 1) {
                Glide.with(context).asBitmap().apply(myOptions).load(lista2.get(pos).getImage_url()).into(iv_2);
                tv_res2.setText(lista2.get(pos).getTitle());
                cv_2.setVisibility(View.VISIBLE);
                cv_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AnimeActivity.class);
                        i.putExtra("anime", (Serializable) lista2.get(pos));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cv_2, ViewCompat.getTransitionName(cv_2));
                        context.startActivity(i, options.toBundle());
                    }
                });
            } else {
                cv_2.setVisibility(View.INVISIBLE);
                Log.d("Falla2", lista2.size() + "");
            }
        }

    }
}
