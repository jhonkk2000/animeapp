package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.GaleriaResource;
import com.jhonkkman.aniappinspiracy.data.models.Picture;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GaleriaFragment extends Fragment {

    private RecyclerView rv_g;
    private LinearLayoutManager lym;
    private AdapterGaleria adapter;
    private ApiAnimeData API_SERVICE;
    private LinearLayout ly_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_galeria, container, false);
        rv_g = view.findViewById(R.id.rv_galeria);
        ly_nodata = view.findViewById(R.id.ly_nodata_galeria);
        loadData();
        return view;
    }

    public void loadData(){
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<GaleriaResource> call = API_SERVICE.getGaleria(AnimeActivity.anime_previous.getMal_id());
        call.enqueue(new Callback<GaleriaResource>() {
            @Override
            public void onResponse(Call<GaleriaResource> call, Response<GaleriaResource> response) {
                if(response.isSuccessful()){
                    ArrayList<Picture> pictures = response.body().getPictures();
                    ArrayList<Picture> lista1 = new ArrayList<>();
                    ArrayList<Picture> lista2 = new ArrayList<>();
                    ArrayList<Picture> lista3 = new ArrayList<>();
                    int pos = 0;
                    for (int i = 0; i < pictures.size()/2; i++) {
                        if(pos==0){
                            lista1.add(pictures.get(i));
                            pos=1;
                        }else{
                            if(pos==1){
                                lista2.add(pictures.get(i));
                                pos=2;
                            }else{
                                lista3.add(pictures.get(i));
                                pos=0;
                            }
                        }
                    }
                    loadGaleria(lista1,lista2,lista3);
                }
            }

            @Override
            public void onFailure(Call<GaleriaResource> call, Throwable t) {

            }
        });
    }

    public void loadGaleria(ArrayList<Picture> lista1,ArrayList<Picture> lista2,ArrayList<Picture> lista3){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterGaleria(lista1,lista2,lista3,getContext());
        rv_g.setLayoutManager(lym);
        rv_g.setAdapter(adapter);
        ly_nodata.setVisibility(View.INVISIBLE);
        rv_g.setVisibility(View.VISIBLE);
    }
}