package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.Character;
import com.jhonkkman.aniappinspiracy.data.models.PersonajesResource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonajesFragment extends Fragment {

    private RecyclerView rv_p;
    private LinearLayoutManager lym;
    private AdapterPersonaje adapter;
    private ApiAnimeData API_SERVICE;
    private LinearLayout ly_nodata;

    public PersonajesFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personajes, container, false);
        rv_p = view.findViewById(R.id.rv_personajes);
        ly_nodata = view.findViewById(R.id.ly_nodata_personajes);
        loadData();
        return view;
    }

    public void loadData(){
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<PersonajesResource> call = API_SERVICE.getPersonajes(AnimeActivity.anime_previous.getMal_id());
        call.enqueue(new Callback<PersonajesResource>() {
            @Override
            public void onResponse(Call<PersonajesResource> call, Response<PersonajesResource> response) {
                if(response.isSuccessful()){
                    ArrayList<Character> characters = response.body().getCharacters();
                    if(characters.size()!=0){
                        ly_nodata.setVisibility(View.INVISIBLE);
                        rv_p.setVisibility(View.VISIBLE);
                    }
                    loadPersonajes(characters);
                }
            }

            @Override
            public void onFailure(Call<PersonajesResource> call, Throwable t) {
                Log.d("FALOOOOOOOOOOOO",t.getMessage());
            }
        });
    }

    public void loadPersonajes(ArrayList<Character> characters){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterPersonaje(getContext(),characters);
        rv_p.setLayoutManager(lym);
        rv_p.setAdapter(adapter);
    }
}