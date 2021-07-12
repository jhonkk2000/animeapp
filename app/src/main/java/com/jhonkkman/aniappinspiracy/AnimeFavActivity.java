package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimeFavActivity extends AppCompatActivity {

    private AppCompatButton btn_save;
    private RecyclerView rv_ani;
    private LinearLayoutManager lym;
    private AdapterAni adapter;
    private ApiAnimeData API_SERVICE;
    private DatabaseReference dbr;
    private SharedPreferences pref;
    private TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_fav);
        btn_save = findViewById(R.id.btn_save_anime);
        rv_ani = findViewById(R.id.rv_ani_fav);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        tv_skip = findViewById(R.id.tv_skip_anime_fav);
        pref = getSharedPreferences("user",MODE_PRIVATE);
        onSave();
        loadData();
        onSkip();
    }

    public void onSkip(){
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbr.child(pref.getString("id","")).child("first_time").setValue(false);
                startActivity(new Intent(AnimeFavActivity.this,GenFavActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void loadData(){
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<AnimeTopResource> call = API_SERVICE.getAnimeTop();
        call.enqueue(new Callback<AnimeTopResource>() {
            @Override
            public void onResponse(Call<AnimeTopResource> call, Response<AnimeTopResource> response) {
                if(response.isSuccessful()){
                    loadAni(response.body().getTop());
                }
            }

            @Override
            public void onFailure(Call<AnimeTopResource> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void onSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> animes_fav = new ArrayList<>();
                int[][] lista = adapter.getAnime_selected();
                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 2; j++) {
                        if(lista[i][j]!=0){
                            animes_fav.add(lista[i][j]);
                        }
                    }
                }
                dbr.child(pref.getString("id","")).child("animes_fav").setValue(animes_fav);
                dbr.child(pref.getString("id","")).child("first_time").setValue(false);
                startActivity(new Intent(AnimeFavActivity.this,GenFavActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void loadAni(List<AnimeItem> list){
        rv_ani.setNestedScrollingEnabled(false);
        lym = new LinearLayoutManager(this);
        adapter = new AdapterAni(this,list);
        rv_ani.setLayoutManager(lym);
        rv_ani.setAdapter(adapter);
        rv_ani.scheduleLayoutAnimation();
    }
}