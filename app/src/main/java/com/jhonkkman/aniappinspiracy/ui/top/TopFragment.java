package com.jhonkkman.aniappinspiracy.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jhonkkman.aniappinspiracy.AdapterPager;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.TodosFragment;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {

    private TabLayout tabs;
    private ViewPager vp_top;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_top, container, false);
        tabs = root.findViewById(R.id.tabs_top);
        vp_top = root.findViewById(R.id.vp_tops);
        loadTabs();
        loadFragments();
        return root;
    }

    public void loadTabs(){
        tabs.addTab(tabs.newTab().setText(getString(R.string.todos)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.en_emision)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.serie)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.peliculas)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.ovas)));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public void loadFragments(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TodosFragment("nada"));
        fragments.add(new TodosFragment("airing"));
        fragments.add(new TodosFragment("tv"));
        fragments.add(new TodosFragment("movie"));
        fragments.add(new TodosFragment("ova"));
        AdapterPager adapterPager = new AdapterPager(getActivity().getSupportFragmentManager(),tabs.getTabCount(),fragments);
        vp_top.setAdapter(adapterPager);
        vp_top.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_top));

    }
}