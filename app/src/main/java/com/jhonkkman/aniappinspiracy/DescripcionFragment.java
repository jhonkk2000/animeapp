package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

public class DescripcionFragment extends Fragment {

    private TextView tv_desc;
    private String desc;
    private ShimmerFrameLayout sm_anime;

    public DescripcionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion, container, false);
        tv_desc = view.findViewById(R.id.tv_desc_anime);
        sm_anime = view.findViewById(R.id.sm_anime_desc);
        sm_anime.startShimmer();
        setRetainInstance(true);
        loadDesc();
        return view;
    }

    public void loadDesc() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(AnimeActivity.load_desc){
                    desc = getArguments().getString("desc");
                    sm_anime.stopShimmer();
                    sm_anime.setVisibility(View.INVISIBLE);
                    tv_desc.setText("Resumen: " + desc);
                    AnimeActivity.load_desc = false;
                }else{
                    loadDesc();
                }
            }
        }, 1000);

    }
}