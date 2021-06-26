package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private boolean busqueda = true;
    private TextView tv_load ;
    private LinearLayout ly_carga, ly_nodata;
    private int episodios;

    public EpisodiosFragment(int episodios){
        if(episodios!=0){
            this.episodios = episodios;
        }else{
            try {
                loadCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        //testEpisodes();
        //verify();
        loadEpisodes();
        return view;
    }

    public void loadCount() throws IOException {
        String[] anime_name = anime_previous.getUrl().split("/");
        ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length-1],0);
        episodios = apiVideoServer.getCountEpisodes();
    }

    /*public void verify(){
        if(busqueda){
            ly_carga.setVisibility(View.VISIBLE);
            rv_episodios.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_load.setText("Se cargaron " + episodios.size() + " de 20 maximos por carga");
                    verify();
                }
            },1000);
        }else{
            ly_carga.setVisibility(View.INVISIBLE);
            if(episodios.size()==0){
                ly_nodata.setVisibility(View.VISIBLE);
            }
            rv_episodios.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    public void testEpisodes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] anime_name = anime_previous.getUrl().split("/");
                int ep = episodios.size()+1;
                do{
                    int val = ep%20;
                    ApiVideoServer apiVideoServer = new ApiVideoServer(anime_name[anime_name.length-1],ep);
                    ep++;
                    ArrayList<String> videos = new ArrayList<>();
                    try {
                        videos = apiVideoServer.getVideoServers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(videos.size()==0){
                        busqueda = false;
                    }else{
                        episodios.add(new Episodio(videos));
                        for (int i = 0; i < animesGuardados.size(); i++) {
                            if(anime_previous.getMal_id()==animesGuardados.get(i).getAnime().getMal_id()){
                                if(animesGuardados.get(i).getEpisodios().size()!=episodios.size()){
                                    animesGuardados.get(i).setEpisodios(episodios);
                                }
                                break;
                            }
                        }
                    }
                }while (busqueda);
            }
        }).start();
    }*/

    public void loadEpisodes(){
        rv_episodios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterEpisodio(getContext(),episodios);
        rv_episodios.setAdapter(adapter);
        //AnimeActivity.dialog.dismissDialog();
    }
}