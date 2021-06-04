package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

public class DescripcionFragment extends Fragment {

    private TextView tv_desc;
    private String desc;

    public DescripcionFragment(String desc){
        this.desc = desc;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion, container, false);
        tv_desc = view.findViewById(R.id.tv_desc_anime);
        loadDesc();
        return view;
    }

    public void loadDesc(){
        AnimeActivity.sm_anime.stopShimmer();
        AnimeActivity.sm_anime.setVisibility(View.INVISIBLE);
        tv_desc.setText("Resumen: " + desc);
    }
}