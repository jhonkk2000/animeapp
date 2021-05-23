package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AnimeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_select_fav;
    private TabLayout tabs;
    private ViewPager vp_anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        toolbar = findViewById(R.id.toolbar_anime);
        toolbar.setTitle(getString(R.string.nombre_de_anime));
        tabs = findViewById(R.id.tabs_anime);
        iv_select_fav = findViewById(R.id.iv_select_fav);
        vp_anime = findViewById(R.id.vp_anime);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onSelectFav();
        loadTabs();
        loadFragments();
    }

    public void onSelectFav(){
        iv_select_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_select_fav.setImageDrawable(getDrawable(R.drawable.ic_fav_lleno_icon));
            }
        });
    }

    public void loadTabs(){
        tabs.addTab(tabs.newTab().setText(getString(R.string.descripcion_tab)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.episodios)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.personajes)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.galeria)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.comentarios)));
    }

    public void loadFragments(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DescripcionFragment());
        fragments.add(new EpisodiosFragment());
        fragments.add(new PersonajesFragment());
        fragments.add(new GaleriaFragment());
        fragments.add(new ComentariosFragment());
        AdapterPager adapter = new AdapterPager(getSupportFragmentManager(),tabs.getTabCount(),fragments);
        vp_anime.setAdapter(adapter);
        vp_anime.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_anime));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}