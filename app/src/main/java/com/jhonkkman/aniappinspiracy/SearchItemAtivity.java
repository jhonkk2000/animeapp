package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.ItemSearch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAtivity extends AppCompatActivity {

    private RecyclerView rv_item_search;
    private ArrayList<ItemSearch> itemSearches = new ArrayList<>();
    private AdapterSearch adapter;
    private Toolbar toolbar;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_ativity);
        rv_item_search = findViewById(R.id.rv_items_search_open);
        toolbar = findViewById(R.id.toolbar_nav_searchitem);
        type = getIntent().getStringExtra("type");
        toolbar.setTitle(type);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadItemsSearches();
    }

    public void loadItemsSearches(){
        rv_item_search.setLayoutManager(new LinearLayoutManager(this));
        loadData();
        adapter = new AdapterSearch(itemSearches,1,this);
        adapter.setAPI_SERVICE(ApiClientData.getClient().create(ApiAnimeData.class));
        adapter.setT(type);
        adapter.setActivity(this);
        rv_item_search.setAdapter(adapter);
    }

    public void loadData(){
        if(type.equals("Generos")){
            for (int i = 0; i < CenterActivity.generos.size(); i++) {
                ItemSearch is = new ItemSearch(CenterActivity.generos.get(i).getName());
                is.setUrl_foto(CenterActivity.generos.get(i).getUrl());
                is.setMall_id(CenterActivity.generos.get(i).getMal_id());
                itemSearches.add(is);
            }
        }else{
            if(type.equals("Letra")){
                for (int i = 1; i < getResources().getStringArray(R.array.letra).length; i++) {
                    ItemSearch is = new ItemSearch(getResources().getStringArray(R.array.letra)[i]);
                    itemSearches.add(is);
                }
            }else{
                if(type.equals("Categoria")){
                    for (int i = 1; i < getResources().getStringArray(R.array.categoria).length; i++) {
                        ItemSearch is = new ItemSearch(getResources().getStringArray(R.array.categoria)[i]);
                        itemSearches.add(is);
                    }
                }else{
                    if(type.equals("Tipo")){
                        for (int i = 1; i < getResources().getStringArray(R.array.tipo).length; i++) {
                            ItemSearch is = new ItemSearch(getResources().getStringArray(R.array.tipo)[i]);
                            itemSearches.add(is);
                        }
                    }else{
                        if(type.equals("Rating")){
                            for (int i = 1; i < getResources().getStringArray(R.array.rating).length; i++) {
                                ItemSearch is = new ItemSearch(getResources().getStringArray(R.array.rating)[i]);
                                itemSearches.add(is);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}