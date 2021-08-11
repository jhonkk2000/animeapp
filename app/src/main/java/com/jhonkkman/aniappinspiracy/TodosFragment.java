package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;
import com.jhonkkman.aniappinspiracy.data.models.TopMemoria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TodosFragment extends Fragment {

    private RecyclerView rv_top;
    private LinearLayoutManager lym;
    private AdapterTodos adapter;
    private String subtype;
    private List<AnimeItem> animes = new ArrayList<>();
    private ProgressBar pb_top;

    public TodosFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todos, container, false);
        rv_top = view.findViewById(R.id.rv_todos_top);
        pb_top = view.findViewById(R.id.pb_top);
        subtype = getArguments().getString("subtype");
        verifyDataSaved();
        return view;
    }

    public void verifyDataSaved(){
        boolean state = false;
        for (int i = 0; i < CenterActivity.animesTop.size(); i++) {
            if(CenterActivity.animesTop.get(i).getSubtype().equals(subtype)){
                state = true;
                animes = CenterActivity.animesTop.get(i).getTop().getTop();
                loadRv(animes);
            }
        }
        if(!state){
            loadTops(subtype);
        }
    }

    public void loadTops(String subtype){
        ApiAnimeData API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<AnimeTopResource> call;
        if(subtype.equals("nada")){
            call = API_SERVICE.getAnimeTop();
        }else{
            call = API_SERVICE.getAnimeTopSubtype(subtype);
        }
        call.enqueue(new Callback<AnimeTopResource>() {
            @Override
            public void onResponse(Call<AnimeTopResource> call, Response<AnimeTopResource> response) {
                if(response.isSuccessful()){
                    CenterActivity.animesTop.add(new TopMemoria(response.body(),subtype));
                    animes = response.body().getTop();
                    loadRv(animes);
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadTops(subtype);
                        }
                    },750);
                }
            }

            @Override
            public void onFailure(Call<AnimeTopResource> call, Throwable t) {

            }
        });
    }

    public void loadRv(List<AnimeItem> animes){
        pb_top.setVisibility(View.INVISIBLE);
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterTodos(getContext(),animes,getActivity());
        rv_top.setLayoutManager(lym);
        rv_top.setAdapter(adapter);
        rv_top.scheduleLayoutAnimation();
    }
}