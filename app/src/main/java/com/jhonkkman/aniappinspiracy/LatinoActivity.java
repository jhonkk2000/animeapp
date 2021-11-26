package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LatinoActivity extends AppCompatActivity {

    private DatabaseReference dbr;
    private RecyclerView rv_latino;
    Toolbar toolbar;
    private AdapterResultados adapter;
    private ArrayList<AnimeItem> list1 = new ArrayList<AnimeItem>();
    private ArrayList<AnimeItem> list2 = new ArrayList<AnimeItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latino);
        rv_latino = findViewById(R.id.rv_latino);
        toolbar = findViewById(R.id.toolbar_nav_latino);
        toolbar.setTitle("Animes Latino");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbr = FirebaseDatabase.getInstance().getReference("animes_latino");
        loadRecycler();
        loadAnimes();
    }

    public void loadRecycler(){
        rv_latino.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterResultados(list1,list2,this,this,"Latino");
        rv_latino.setAdapter(adapter);
    }

    public void loadAnimes(){
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list1.clear();
                    list2.clear();
                    int pos = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Log.d("ProbandoLatino",ds.getKey());
                        if(pos==0){
                            list1.add(ds.getValue(AnimeItem.class));
                            pos=1;
                        }else{
                            list2.add(ds.getValue(AnimeItem.class));
                            pos=0;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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