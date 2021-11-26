package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jhonkkman.aniappinspiracy.data.api.ApiVideoServer;
import com.jhonkkman.aniappinspiracy.data.models.Episodio;
import com.jhonkkman.aniappinspiracy.ui.inicio.InicioFragment;

import java.io.IOException;
import java.util.ArrayList;

import static com.jhonkkman.aniappinspiracy.AnimeActivity.anime_previous;
import static com.jhonkkman.aniappinspiracy.CenterActivity.animesGuardados;

public class EpisodiosFragment extends Fragment {

    private RecyclerView rv_episodios;
    private AdapterEpisodio adapter;
    private boolean busqueda = false;
    private TextView tv_load;
    private LinearLayout ly_carga, ly_nodata;
    private int episodios;
    private String url="";
    private ProgressBar pb_episodes;
    //private boolean avaibleLat;

    public EpisodiosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episodios, container, false);
        rv_episodios = view.findViewById(R.id.rv_episodios);
        ly_carga = view.findViewById(R.id.ly_carga);
        ly_nodata = view.findViewById(R.id.ly_nodata_episodios);
        tv_load = view.findViewById(R.id.tv_episodios_cargando);
        ly_carga.setVisibility(View.INVISIBLE);
        ly_nodata.setVisibility(View.INVISIBLE);
        rv_episodios.setVisibility(View.VISIBLE);
        pb_episodes = view.findViewById(R.id.pb_episodes);
        episodios = getArguments().getInt("ep");
        //avaibleLat = getArguments().getBoolean("al");
        url = getArguments().getString("url");
        //testEpisodes();
        //verify();
        Log.d("ARGUMENTO EP", "" + getArguments().getInt("ep"));
        loadEpisodes();
        return view;
    }

    public void loadCount() throws IOException {
        String[] anime_name = anime_previous.getUrl().split("/");
        ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length - 1], 0);
        episodios = apiVideoServer.getCountEpisodes();
        Bundle bundle = new Bundle();
        bundle.putInt("ep", episodios);
        this.setArguments(bundle);
        busqueda = true;
    }

    public void loadEpisodes() {
        if (episodios == 0) {
            loading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadCount();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            loadRecycler();
        }
    }

    public void loading(){
        if(busqueda){
            //Log.d("ARGUMENTO EP", "entrando" + busqueda);
            loadRecycler();
        }else{
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading();
                }
            },400);
        }
    }

    public void loadRecycler(){
        pb_episodes.setVisibility(View.INVISIBLE);
        rv_episodios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterEpisodio(getContext(), episodios,url);
        rv_episodios.setAdapter(adapter);
    }
}