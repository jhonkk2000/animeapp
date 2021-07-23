package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jhonkkman.aniappinspiracy.ui.explora.ExploraFragment;
import com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment;
import com.jhonkkman.aniappinspiracy.ui.inicio.InicioFragment;
import com.jhonkkman.aniappinspiracy.ui.perfil.PerfilFragment;
import com.jhonkkman.aniappinspiracy.ui.top.TopFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnimesFragment extends Fragment {

    private BottomNavigationView bnv;
    private InicioFragment inicioFragment = new InicioFragment();
    private ExploraFragment exploraFragment = new ExploraFragment();
    private TopFragment topFragment = new TopFragment();
    private FavoritoFragment favoritoFragment = new FavoritoFragment();
    private PerfilFragment perfilFragment = new PerfilFragment();
    private NonSwipeableViewPager vp_animes;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animes, container, false);
        bnv = view.findViewById(R.id.bnv_animes);
        vp_animes = view.findViewById(R.id.container_animes);
        vp_animes.setOffscreenPageLimit(5);
        loadViewPager();
        openFragments(0);
        bottomNavigationOnClick();
        return view;
    }

    public void bottomNavigationOnClick(){
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_inicio:
                        openFragments(0);
                        break;
                    case R.id.nav_explora:
                        openFragments(1);
                        break;
                    case R.id.nav_top:
                        openFragments(2);
                        break;
                    case R.id.nav_fav:
                        openFragments(3);
                        break;
                    case R.id.nav_perfil:
                        openFragments(4);
                        break;
                }
                return true;
            }
        });
    }


    public void openFragments(int pos){
        /*Fragment fragment2 = getActivity().getSupportFragmentManager().findFragmentByTag("inicio");
        if (fragment2 != null && fragment2.isVisible()) {
            Toast.makeText(getContext(), "caca", Toast.LENGTH_SHORT).show();
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_animes, fragment);
        transaction.addToBackStack(null);
        transaction.commit();*/
        float scale = getResources().getDisplayMetrics().density;
        int veinteDP = (int) (20*scale + 0.5f);
        int seDP = (int) (60*scale + 0.5f);
        if(pos!=0){
            if(pos==1){
                vp_animes.setPadding(veinteDP,seDP,veinteDP,0);
            }else{
                vp_animes.setPadding(0,seDP,0,0);
            }
        }else{
            vp_animes.setPadding(0,0,0,0);
        }
        vp_animes.setCurrentItem(pos);
    }

    public void loadViewPager(){
        fragments.add(inicioFragment);
        fragments.add(exploraFragment);
        fragments.add(topFragment);
        fragments.add(favoritoFragment);
        fragments.add(perfilFragment);
        AdapterPager adapter = new AdapterPager(getActivity().getSupportFragmentManager(),fragments.size(),fragments);
        vp_animes.setAdapter(adapter);
    }
}