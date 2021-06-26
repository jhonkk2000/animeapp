package com.jhonkkman.aniappinspiracy.ui.favorito;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jhonkkman.aniappinspiracy.AdapterPager;
import com.jhonkkman.aniappinspiracy.FavUserFragment;
import com.jhonkkman.aniappinspiracy.GenUserFragment;
import com.jhonkkman.aniappinspiracy.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritoFragment extends Fragment {

    private TabLayout tabs;
    private ViewPager vp_fav;
    public static List<Long> animesFav = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorito, container, false);
        tabs = view.findViewById(R.id.tabs_favorito);
        vp_fav = view.findViewById(R.id.vp_favorito);
        loadTabs();
        loadFragments();

        return view;
    }

    public void loadTabs(){
        tabs.addTab(tabs.newTab().setText(getString(R.string.animes)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.generos)));
    }

    public void loadFragments(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FavUserFragment(animesFav));
        fragments.add(new GenUserFragment());
        AdapterPager adapterPager = new AdapterPager(getActivity().getSupportFragmentManager(),tabs.getTabCount(),fragments);
        vp_fav.setAdapter(adapterPager);
        vp_fav.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_fav));
    }
}