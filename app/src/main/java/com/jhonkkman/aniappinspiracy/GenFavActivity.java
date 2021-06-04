package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenFavActivity extends AppCompatActivity {

    private RecyclerView rv_gen;
    private LinearLayoutManager lym;
    private AdapterGen adapter;
    private AppCompatButton btn_save;
    private TextView tv_skip;
    private DatabaseReference dbr;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_fav);
        rv_gen = findViewById(R.id.rv_gen_fav);
        btn_save = findViewById(R.id.btn_save_gen);
        tv_skip = findViewById(R.id.tv_skip_gen_fav);
        dbr = FirebaseDatabase.getInstance().getReference();
        pref = getSharedPreferences("user",MODE_PRIVATE);
        onSave();
        onSkip();
        readGenres();
    }

    public void onSkip(){
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GenFavActivity.this,CenterActivity.class));
                finish();
            }
        });
    }

    public void readGenres(){
        dbr.child("genres").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<GeneroItem> generos = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        generos.add(ds.getValue(GeneroItem.class));
                    }
                    loadGen(generos);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void onSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> gen_fav = new ArrayList<>();
                int[] generos = adapter.getSelect();
                for (int i = 0; i < generos.length; i++) {
                    if(generos[i]==1){
                        gen_fav.add(i+1);
                    }
                }
                dbr.child("users").child(pref.getString("id","")).child("gen_fav").setValue(gen_fav);
                startActivity(new Intent(GenFavActivity.this,CenterActivity.class));
                finish();
            }
        });
    }

    public void loadGen(List<GeneroItem> generos){
        lym = new LinearLayoutManager(this);
        adapter = new AdapterGen(generos);
        rv_gen.setLayoutManager(lym);
        rv_gen.setAdapter(adapter);
        rv_gen.setNestedScrollingEnabled(false);
        rv_gen.scheduleLayoutAnimation();
    }
}