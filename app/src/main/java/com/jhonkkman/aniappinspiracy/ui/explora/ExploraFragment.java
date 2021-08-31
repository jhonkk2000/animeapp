package com.jhonkkman.aniappinspiracy.ui.explora;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
import com.jhonkkman.aniappinspiracy.AdapterSearch;
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
import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;
import com.jhonkkman.aniappinspiracy.data.models.ItemSearch;

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

import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;

public class ExploraFragment extends Fragment {

    private LinearLayoutManager lym;
    private AdapterResultados adapter;
    private EditText et_nombre_anime;
    private ImageButton btn_buscar;
    private ApiAnimeData API_SERVICE;
    private final AlertLoading dialog = new AlertLoading();
    private RecyclerView rv_search_items;
    private AdapterSearch adapterSearch;
    private ArrayList<ItemSearch> itemSearches = new ArrayList<>();
    private InterstitialAd mInterstitialAd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explora, container, false);
        rv_search_items = root.findViewById(R.id.rv_search_items);
        et_nombre_anime = root.findViewById(R.id.et_nombre_anime);
        btn_buscar = root.findViewById(R.id.btn_buscar);
        loadGeneros();
        loadSearchItems();
        //loadAnime();
        onBuscar();
        return root;
    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }

    public void loadSearchItems() {
        itemSearches.add(new ItemSearch(getString(R.string.generos)));
        itemSearches.add(new ItemSearch("Letra"));
        itemSearches.add(new ItemSearch("Categoria"));
        itemSearches.add(new ItemSearch("Tipo"));
        itemSearches.add(new ItemSearch("Rating"));
        rv_search_items.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterSearch = new AdapterSearch(itemSearches, 0, getContext());
        rv_search_items.setAdapter(adapterSearch);
        adapterSearch.notifyDataSetChanged();
    }

    public void onBuscar() {
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_nombre_anime.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Elija una opcion a buscar", Toast.LENGTH_SHORT).show();
                } else {
                    if (!et_nombre_anime.getText().toString().equals("")) {
                        String busqueda = et_nombre_anime.getText().toString();
                        if (busqueda.length() >= 3) {
                            dialog.showDialog(getActivity(), "Buscando");
                            loadAd();
                            ApiClientData.cargarLlamada(0, busqueda, API_SERVICE, getContext(), getActivity(), et_nombre_anime.getText().toString(), dialog,1);
                        } else {
                            Toast.makeText(getContext(), "Ingrese minimo 3 caracteres para la busqueda", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Ingrese minimo 3 caracteres para la busqueda", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressLint("ResourceType")
    public void loadGeneros() {
        String[] genres = new String[generos.size() + 1];
        genres[0] = "Generos";
        Log.d("genres", genres.length + "");
        for (int i = 0; i < generos.size(); i++) {
            genres[i + 1] = generos.get(i).getName();
        }
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, genres);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

}