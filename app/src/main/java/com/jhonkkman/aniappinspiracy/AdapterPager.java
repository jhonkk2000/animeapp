package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class AdapterPager extends FragmentStatePagerAdapter {

    int numItems;
    List<Fragment> fragments;

    public AdapterPager(@NonNull FragmentManager fm, int numItems, List<Fragment> fragments) {
        super(fm);
        this.numItems = numItems;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            case 3:
                return fragments.get(3);
            case 4:
                return fragments.get(4);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numItems;
    }
}
