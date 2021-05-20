package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterPagerTop extends FragmentStatePagerAdapter {

    int numItems;

    public AdapterPagerTop(@NonNull FragmentManager fm, int numItems) {
        super(fm);
        this.numItems = numItems;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TodosFragment();
            case 1:
                return new TodosFragment();
            case 2:
                return new TodosFragment();
            case 3:
                return new TodosFragment();
            case 4:
                return new TodosFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numItems;
    }
}
