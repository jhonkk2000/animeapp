package com.jhonkkman.aniappinspiracy.ui.explora;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.AdapterGaleria;
import com.jhonkkman.aniappinspiracy.AdapterPager;
import com.jhonkkman.aniappinspiracy.AdapterResultados;
import com.jhonkkman.aniappinspiracy.AlertLoading;
import com.jhonkkman.aniappinspiracy.AnimeActivity;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.ComentariosFragment;
import com.jhonkkman.aniappinspiracy.DescripcionFragment;
import com.jhonkkman.aniappinspiracy.EpisodiosFragment;
import com.jhonkkman.aniappinspiracy.GaleriaFragment;
import com.jhonkkman.aniappinspiracy.PersonajesFragment;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploraFragment extends Fragment {

    private RecyclerView rv_resultados;
    private LinearLayoutManager lym;
    private AdapterResultados adapter;
    private MaterialCardView cv_best_anime;
    private Spinner sp_generos,sp_categoria,sp_tipo,sp_rating,sp_letra;
    private EditText et_nombre_anime;
    private ImageButton btn_buscar;
    private ApiAnimeData API_SERVICE;
    private ImageView iv_busqueda;
    private TextView tv_nombre,tv_desc,tv_emision,tv_fecha,tv_tipo,tv_puntaje;
    private final AlertLoading dialog = new AlertLoading();
    private NestedScrollView scroll;
    private LinearLayout ly_nodata;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explora, container, false);
        rv_resultados = root.findViewById(R.id.rv_mas_resultados);
        scroll = root.findViewById(R.id.scroll_busqueda);
        ly_nodata = root.findViewById(R.id.ly_nodata_busqueda);
        cv_best_anime = root.findViewById(R.id.cv_best_anime);
        et_nombre_anime = root.findViewById(R.id.et_nombre_anime);
        sp_generos = root.findViewById(R.id.sp_generos);
        sp_rating = root.findViewById(R.id.sp_rating);
        sp_categoria = root.findViewById(R.id.sp_categoria);
        sp_letra = root.findViewById(R.id.sp_letra);
        sp_tipo = root.findViewById(R.id.sp_tipo);
        btn_buscar = root.findViewById(R.id.btn_buscar);
        iv_busqueda = root.findViewById(R.id.iv_anime_busqueda);
        tv_desc = root.findViewById(R.id.tv_desc_buscar);
        tv_nombre = root.findViewById(R.id.tv_nombre_buscar);
        tv_emision = root.findViewById(R.id.tv_en_emision_buscar);
        tv_fecha = root.findViewById(R.id.tv_fecha_buscar);
        tv_tipo = root.findViewById(R.id.tv_tipo_buscar);
        tv_puntaje = root.findViewById(R.id.tv_puntaje_buscar);
        loadGeneros();
        //loadAnime();
        eventsFilter();
        onBuscar();
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
                    if(!et_nombre_anime.getText().toString().equals("")){
                        et_nombre_anime.setText("");
                    }
                }
                Log.d("CAMBIOO tipo", i+"");
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
                    if(!et_nombre_anime.getText().toString().equals("")){
                        et_nombre_anime.setText("");
                    }
                }
                Log.d("CAMBIOO categoria", i+"");
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
                    if(!et_nombre_anime.getText().toString().equals("")){
                        et_nombre_anime.setText("");
                    }
                }
                Log.d("CAMBIOO rating", i+"");
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
                    if(!et_nombre_anime.getText().toString().equals("")){
                        et_nombre_anime.setText("");
                    }
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
                    if(!et_nombre_anime.getText().toString().equals("")){
                        et_nombre_anime.setText("");
                    }
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
                    if(sp_letra.getSelectedItemPosition()!=0){
                        sp_letra.setSelection(0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onBuscar(){
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_nombre_anime.getText().toString().equals("") && sp_categoria.getSelectedItemPosition()==0 && sp_generos.getSelectedItemPosition()==0 && sp_letra.getSelectedItemPosition()==0 &&
                sp_rating.getSelectedItemPosition()==0&&sp_tipo.getSelectedItemPosition()==0){
                    Toast.makeText(getContext(), "Elija una opcion a buscar", Toast.LENGTH_SHORT).show();
                }else{
                    if(!et_nombre_anime.getText().toString().equals("")){
                        String busqueda = et_nombre_anime.getText().toString();
                        if(busqueda.length()>=3){
                            dialog.showDialog(getActivity(),"Buscando");
                            cargarLlamada(0,busqueda);
                        }else{
                            Toast.makeText(getContext(), "Ingrese minimo 3 caracteres para la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(sp_letra.getSelectedItemPosition()!=0){
                            dialog.showDialog(getActivity(),"Buscando");
                            cargarLlamada(1,sp_letra.getSelectedItem().toString());
                        }else{
                            if(sp_tipo.getSelectedItemPosition()!=0){
                                dialog.showDialog(getActivity(),"Buscando");
                                cargarLlamada(2,sp_tipo.getSelectedItem().toString());
                            }else{
                                if(sp_categoria.getSelectedItemPosition()!=0){
                                    dialog.showDialog(getActivity(),"Buscando");
                                    switch (sp_categoria.getSelectedItemPosition()){
                                        case 1:
                                            cargarLlamada(3,"g");
                                            break;
                                        case 2:
                                            cargarLlamada(3,"pg");
                                            break;
                                        case 3:
                                            cargarLlamada(3,"pg13");
                                            break;
                                        case 4:
                                            cargarLlamada(3,"r17");
                                            break;
                                    }
                                }else{
                                    if(sp_rating.getSelectedItemPosition()!=0){
                                        dialog.showDialog(getActivity(),"Buscando");
                                        cargarLlamada(4,sp_rating.getSelectedItem().toString());
                                    }else{
                                        if(sp_generos.getSelectedItemPosition()!=0){
                                            dialog.showDialog(getActivity(),"Buscando");
                                            String id = "";
                                            for (int i = 0; i < CenterActivity.generos.size(); i++) {
                                                if(sp_generos.getSelectedItem().toString().equals(CenterActivity.generos.get(i).getName())){
                                                    id= String.valueOf(CenterActivity.generos.get(i).getMal_id());
                                                }
                                            }
                                            cargarLlamada(5,id);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    public void cargarLlamada(int tipo,String busqueda){
        switch (tipo){
            case 0:
                Call<AnimeSearchRequest> call = API_SERVICE.getAnimeSearch(busqueda);
                call.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1:
                Call<AnimeSearchRequest> call2 = API_SERVICE.getAnimeLetter(busqueda);
                call2.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2:
                Call<AnimeSearchRequest> call6 = API_SERVICE.getAnimeType(busqueda);
                call6.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 3:
                Call<AnimeSearchRequest> call3 = API_SERVICE.getAnimeRated(busqueda);
                call3.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 4:
                Call<AnimeSearchRequest> call4 = API_SERVICE.getAnimeScore(Float.parseFloat(busqueda));
                call4.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 5:
                Call<AnimeSearchRequest> call5 = API_SERVICE.getAnimeGenre(Integer.parseInt(busqueda));
                call5.enqueue(new Callback<AnimeSearchRequest>() {
                    @Override
                    public void onResponse(Call<AnimeSearchRequest> call, Response<AnimeSearchRequest> response) {
                        if(response.isSuccessful()){
                            List<AnimeItem> animeItems = response.body().getAnimeItems();
                            if(animeItems.size()!=0){
                                loadResultados(animeItems);
                            }else{
                                ly_nodata.setVisibility(View.VISIBLE);
                                scroll.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            dialog.dismissDialog();
                            Toast.makeText(getContext(), "Algo fallo. Vuelve a intentar la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeSearchRequest> call, Throwable t) {
                        dialog.dismissDialog();
                        Toast.makeText(getContext(), "Error al buscar, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }

    public void loadResultados(List<AnimeItem> animeItems){
        AnimeItem anime1 = animeItems.get(0);
        loadAnime(anime1);
        ArrayList<AnimeItem> lista1 = new ArrayList<>();
        ArrayList<AnimeItem> lista2 = new ArrayList<>();
        ArrayList<AnimeItem> lista3 = new ArrayList<>();
        Glide.with(getContext()).load(anime1.getImage_url()).into(iv_busqueda);
        tv_nombre.setText(anime1.getTitle());
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        final Translator translator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(Void unused) {
                translator.translate(anime1.getSynopsis()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        tv_desc.setText(s);
                        if(anime1.isAiring()){
                            tv_emision.setText("En emision");
                        }else{
                            tv_emision.setText("Finalizado");
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+SS:SS", Locale.ENGLISH);
                        LocalDate localDate = LocalDate.parse(anime1.getStart_date(), formatter);
                        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        tv_fecha.setText("Fecha de estreno: " + sdf.format(date));
                        tv_tipo.setText(anime1.getType());
                        tv_puntaje.setText(String.valueOf(anime1.getScore()));
                        int pos = 0;
                        for (int i = 0; i < animeItems.size(); i++) {
                            if(i!=0){
                                if(pos==0){
                                    lista1.add(animeItems.get(i));
                                    pos=1;
                                }else{
                                    if(pos==1){
                                        lista2.add(animeItems.get(i));
                                        pos=2;
                                    }else{
                                        lista3.add(animeItems.get(i));
                                        pos=0;
                                    }
                                }
                            }
                        }
                        lym = new LinearLayoutManager(getContext());
                        adapter = new AdapterResultados(lista1,lista2,lista3,getContext(),getActivity());
                        rv_resultados.setLayoutManager(lym);
                        rv_resultados.setAdapter(adapter);
                        rv_resultados.setNestedScrollingEnabled(false);
                        ly_nodata.setVisibility(View.INVISIBLE);
                        scroll.setVisibility(View.VISIBLE);
                        dialog.dismissDialog();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("TRADUCTOR","fallo");
            }
        });
        //rv_resultados.setNestedScrollingEnabled(false);
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

    public void loadAnime(AnimeItem anime){
        cv_best_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),AnimeActivity.class);
                i.putExtra("anime", (Serializable) anime);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), cv_best_anime, ViewCompat.getTransitionName(cv_best_anime));
                getActivity().startActivity(i,options.toBundle());
            }
        });
    }
}