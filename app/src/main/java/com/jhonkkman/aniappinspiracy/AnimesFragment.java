package com.jhonkkman.aniappinspiracy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.ui.explora.ExploraFragment;
import com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment;
import com.jhonkkman.aniappinspiracy.ui.inicio.InicioFragment;
import com.jhonkkman.aniappinspiracy.ui.perfil.PerfilFragment;
import com.jhonkkman.aniappinspiracy.ui.top.TopFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;

public class AnimesFragment extends Fragment {

    public static BottomNavigationView bnv;
    private InicioFragment inicioFragment = new InicioFragment();
    private ExploraFragment exploraFragment = new ExploraFragment();
    private TopFragment topFragment = new TopFragment();
    public static FavoritoFragment favoritoFragment = new FavoritoFragment();
    private PerfilFragment perfilFragment = new PerfilFragment();
    private NonSwipeableViewPager vp_animes;
    private List<Fragment> fragments = new ArrayList<>();
    public static int pos_bototm_nav = 0;
    public int last_item = 0;
    private boolean state_no_session = false;
    private DatabaseReference dbr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animes, container, false);
        bnv = view.findViewById(R.id.bnv_animes);
        vp_animes = view.findViewById(R.id.container_animes);
        vp_animes.setOffscreenPageLimit(4);
        dbr = FirebaseDatabase.getInstance().getReference("season");
        loadViewPager();
        openFragments(0, R.id.nav_inicio);
        bottomNavigationOnClick();
        return view;
    }

    public void bottomNavigationOnClick() {
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_inicio:
                        openFragments(0, R.id.nav_inicio);
                        break;
                    case R.id.nav_explora:
                        openFragments(1, R.id.nav_explora);
                        break;
                    case R.id.nav_top:
                        openFragments(2, R.id.nav_top);
                        break;
                    case R.id.nav_fav:
                        if (CenterActivity.login) {
                            openFragments(3, R.id.nav_fav);
                        } else {
                            bnv.setSelectedItemId(last_item);
                            if (!state_no_session) {
                                Toast.makeText(getContext(), "No haz iniciado sesión", Toast.LENGTH_SHORT).show();
                                state_no_session = true;
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    public void openFragments(int pos, int last) {
        pos_bototm_nav = pos;
        last_item = last;
        vp_animes.setCurrentItem(pos);
    }

    public void loadViewPager() {
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Bundle bundle = new Bundle();
                bundle.putString("season", snapshot.child("nombre").getValue().toString());
                bundle.putInt("year", Integer.parseInt(snapshot.child("year").getValue().toString()));
                inicioFragment.setArguments(bundle);
                fragments.add(inicioFragment);
                fragments.add(exploraFragment);
                fragments.add(topFragment);
                if (CenterActivity.login) {
                    fragments.add(favoritoFragment);
                }
                AdapterPager adapter = new AdapterPager(getActivity().getSupportFragmentManager(), fragments.size(), fragments);
                vp_animes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}