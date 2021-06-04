package com.jhonkkman.aniappinspiracy.ui.explora;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AnimeActivity;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.MainActivity;
import com.jhonkkman.aniappinspiracy.R;

public class ExploraFragment extends Fragment {

    private RecyclerView rv_resultados;
    private LinearLayoutManager lym;
    private AdapterResultados adapter;
    private MaterialCardView cv_best_anime;
    private Spinner sp_generos,sp_categoria,sp_tipo,sp_rating,sp_letra;
    private EditText et_nombre_anime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explora, container, false);
        rv_resultados = root.findViewById(R.id.rv_mas_resultados);
        cv_best_anime = root.findViewById(R.id.cv_best_anime);
        et_nombre_anime = root.findViewById(R.id.et_nombre_anime);
        sp_generos = root.findViewById(R.id.sp_generos);
        sp_rating = root.findViewById(R.id.sp_rating);
        sp_categoria = root.findViewById(R.id.sp_categoria);
        sp_letra = root.findViewById(R.id.sp_letra);
        sp_tipo = root.findViewById(R.id.sp_tipo);
        //loadResultados();
        loadGeneros();
        loadAnime();
        eventsFilter();
        return root;
    }

    public void eventsFilter(){
        sp_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(sp_letra.getSelectedItemPosition()!=0){
                        sp_letra.setSelection(0);
                    }
                    if(sp_categoria.getSelectedItemPosition()!=0){
                        sp_categoria.setSelection(0);
                    }
                    if(sp_rating.getSelectedItemPosition()!=0){
                        sp_rating.setSelection(0);
                    }
                    if(sp_generos.getSelectedItemPosition()!=0){
                        sp_generos.setSelection(0);
                    }
                    et_nombre_anime.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(sp_tipo.getSelectedItemPosition()!=0){
                        sp_tipo.setSelection(0);
                    }
                    if(sp_letra.getSelectedItemPosition()!=0){
                        sp_letra.setSelection(0);
                    }
                    if(sp_rating.getSelectedItemPosition()!=0){
                        sp_rating.setSelection(0);
                    }
                    if(sp_generos.getSelectedItemPosition()!=0){
                        sp_generos.setSelection(0);
                    }
                    et_nombre_anime.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(sp_tipo.getSelectedItemPosition()!=0){
                        sp_tipo.setSelection(0);
                    }
                    if(sp_categoria.getSelectedItemPosition()!=0){
                        sp_categoria.setSelection(0);
                    }
                    if(sp_letra.getSelectedItemPosition()!=0){
                        sp_letra.setSelection(0);
                    }
                    if(sp_generos.getSelectedItemPosition()!=0){
                        sp_generos.setSelection(0);
                    }
                    et_nombre_anime.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_generos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(sp_tipo.getSelectedItemPosition()!=0){
                        sp_tipo.setSelection(0);
                    }
                    if(sp_categoria.getSelectedItemPosition()!=0){
                        sp_categoria.setSelection(0);
                    }
                    if(sp_rating.getSelectedItemPosition()!=0){
                        sp_rating.setSelection(0);
                    }
                    if(sp_letra.getSelectedItemPosition()!=0){
                        sp_letra.setSelection(0);
                    }
                    et_nombre_anime.setText("");
                }
                Log.d("CAMBIOO generos", i+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_letra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(sp_tipo.getSelectedItemPosition()!=0){
                        sp_tipo.setSelection(0);
                    }
                    if(sp_categoria.getSelectedItemPosition()!=0){
                        sp_categoria.setSelection(0);
                    }
                    if(sp_rating.getSelectedItemPosition()!=0){
                        sp_rating.setSelection(0);
                    }
                    if(sp_generos.getSelectedItemPosition()!=0){
                        sp_generos.setSelection(0);
                    }
                    et_nombre_anime.setText("");
                }
                Log.d("CAMBIOO letra", i+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_nombre_anime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals("")){
                    sp_categoria.setSelection(0);
                    sp_rating.setSelection(0);
                    sp_generos.setSelection(0);
                    sp_tipo.setSelection(0);
                    sp_letra.setSelection(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void loadResultados(){
        lym = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //adapter = new AdapterResultados();
        rv_resultados.setLayoutManager(lym);
        rv_resultados.setAdapter(adapter);
    }

    public void loadGeneros(){
        String[] genres = new String[CenterActivity.generos.size()+1];
        genres[0]= "Generos";
        Log.d("genres",genres.length+"");
        for (int i = 0; i < CenterActivity.generos.size(); i++) {
            genres[i+1] = CenterActivity.generos.get(i).getName();
        }
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,genres);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_generos.setAdapter(aa);
    }

    public void loadAnime(){
        cv_best_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), cv_best_anime, ViewCompat.getTransitionName(cv_best_anime));
                startActivity(new Intent(getContext(), AnimeActivity.class),options.toBundle());
            }
        });
    }
}