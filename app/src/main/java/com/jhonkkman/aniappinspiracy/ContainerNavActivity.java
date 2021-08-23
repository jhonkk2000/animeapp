package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.jhonkkman.aniappinspiracy.ui.acerca.AcercaFragment;
import com.jhonkkman.aniappinspiracy.ui.configuracion.ConfiguracionFragment;
import com.jhonkkman.aniappinspiracy.ui.perfil.PerfilFragment;

public class ContainerNavActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_nav);
        toolbar = findViewById(R.id.toolbar_nav_items);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        switch (getIntent().getStringExtra("fragment")){
            case "c":
                toolbar.setTitle("Configuracion");
                loadFragments(new ConfiguracionFragment());
                break;
            case "a":
                toolbar.setTitle("Acerca de");
                loadFragments(new AcercaFragment());
                break;
            case "p":
                toolbar.setTitle("Perfil");
                loadFragments(new PerfilFragment());
                break;
        }
    }

    public void loadFragments(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_nav_items,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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