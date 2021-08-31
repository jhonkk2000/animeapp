package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jhonkkman.aniappinspiracy.data.api.ApiAnimeData;
import com.jhonkkman.aniappinspiracy.data.api.ApiClientData;
import com.jhonkkman.aniappinspiracy.data.models.AnimeCompleto;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.Episodio;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhonkkman.aniappinspiracy.CenterActivity.animesGuardados;

public class AnimeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_select_fav,iv_anime,iv_share;
    private TabLayout tabs;
    private ViewPager vp_anime;
    public static AnimeItem anime_previous;
    private TextView tv_title,tv_year,tv_generos,tv_en_emision,tv_finalizado,tv_stars;
    private ApiAnimeData API_SERVICE;
    private YouTubePlayerView yt_trailer;
    private DatabaseReference dbr;
    private SharedPreferences pref;
    public static String KEY_COMENTARIO = "";
    public static AlertLoading dialog = new AlertLoading();
    public static boolean load_desc = false;
    //public static ArrayList<Episodio> episodios = new ArrayList<>();
    private AnimeResource anime = new AnimeResource();
    private ArrayList<Episodio> episodios = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private boolean change_data = false;
    private InterstitialAd mInterstitialAd;
    private DescripcionFragment descF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_anime);
        toolbar = findViewById(R.id.toolbar_anime);
        toolbar.setTitle(getString(R.string.nombre_de_anime));
        tabs = findViewById(R.id.tabs_anime);
        iv_select_fav = findViewById(R.id.iv_select_fav);
        iv_anime = findViewById(R.id.iv_anime);
        vp_anime = findViewById(R.id.vp_anime);
        tv_title = findViewById(R.id.tv_title_anime);
        iv_share = findViewById(R.id.iv_share);
        tv_year = findViewById(R.id.tv_year_anime);
        tv_generos = findViewById(R.id.tv_generos_anime);
        yt_trailer = findViewById(R.id.yt_trailer);
        tv_en_emision = findViewById(R.id.tv_en_emision);
        tv_finalizado = findViewById(R.id.tv_finalizado);
        tv_stars = findViewById(R.id.tv_stars_anime);
        pref = getSharedPreferences("user",MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        anime_previous = (AnimeItem) getIntent().getSerializableExtra("anime");
        vp_anime.setOffscreenPageLimit(5);
        //dialog.showDialog(this,"Cargando episodios");
        loadAd();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if(CenterActivity.login){
            //loadKeyComentario();
            onSelectFav();
        }
        onShare();
        loadTabs();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFragments();
            }
        },800);
        savedLoadState();
        loadData();
    }

    public void onShare(){
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendI = new Intent();
                sendI.setAction(Intent.ACTION_SEND);
                sendI.putExtra(Intent.EXTRA_TEXT,"Descarga ANIME SHOW, y mira " + anime_previous.getTitle() +" asi como muchos otros animes más: https://play.google.com/store/apps/details?id=com.jhonkkman.aniappinspiracy");
                sendI.setType("text/plain");

                Intent shareI = Intent.createChooser(sendI,null);
                startActivity(shareI);
            }
        });
    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AnimeActivity.this);
                } else {
                    Log.d("TAGAD", "The interstitial ad wasn't ready yet.");
                }
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAGAD", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAGAD", "The ad failed to show.");
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
                Log.d("TAGAD", loadAdError.toString());
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if(change_data){
            animesGuardados.add(new AnimeCompleto(anime,episodios));
        }else{
            for (int i = 0; i < animesGuardados.size(); i++) {
                if(anime_previous.getMal_id()==animesGuardados.get(i).getAnime().getMal_id()){
                    if(animesGuardados.get(i).getEpisodios().size()!=episodios.size()){
                        animesGuardados.get(i).setEpisodios(episodios);
                    }
                    break;
                }
            }
        }
    }

    public void savedLoadState(){
        boolean state = false;
        for (int i = 0; i < animesGuardados.size(); i++) {
            if(anime_previous.getMal_id()==animesGuardados.get(i).getAnime().getMal_id()){
                state = true;
                anime = animesGuardados.get(i).getAnime();
                episodios = animesGuardados.get(i).getEpisodios();
                loadDataAnime();
                traducir();
                break;
            }
        }
        if(!state){
            loadAnime();
        }
    }

    public void loadKeyComentario(){
        dbr.getParent().child("anime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(Integer.parseInt(ds.child("mall_id").getValue().toString())==anime_previous.getMal_id()){
                            KEY_COMENTARIO = ds.getKey();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void loadData(){
        Glide.with(this).load(anime_previous.getImage_url()).into(iv_anime);
        toolbar.setTitle(anime_previous.getTitle());
        tv_title.setText(anime_previous.getTitle());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onSelectFav(){
        List<Integer> animesFav;
        Gson gson = new Gson();
        String json = pref.getString("usuario","");
        User user = gson.fromJson(json,User.class);
        //animesFav = user.getAnime_fav();
        /*for (int i = 0; i < animesFav.size(); i++) {
            if(anime_previous.getMal_id()==animesFav.get(i)){
                iv_select_fav.setImageDrawable(getDrawable(R.drawable.ic_fav_lleno_icon));
                break;
            }
        }*/
        final ArrayList[] animesFavUpdate = new ArrayList[1];
        //animesFavUpdate[0] = (ArrayList) animesFav;
        dbr.child(pref.getString("id","")).child("animes_fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean state = false;
                if(snapshot.exists()){
                    ArrayList<Integer> lista2 = (ArrayList<Integer>) snapshot.getValue();
                    animesFavUpdate[0] = lista2;
                    for(DataSnapshot ds:snapshot.getChildren()){
                        if(anime_previous.getMal_id()== (Integer.parseInt(ds.getValue().toString()))){
                            state = true;
                            break;
                        }
                    }
                }
                if(state){
                    iv_select_fav.setImageDrawable(getDrawable(R.drawable.ic_fav_lleno_icon));
                }else{
                    iv_select_fav.setImageDrawable(getDrawable(R.drawable.ic_fav_icon));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        try {
            iv_select_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(iv_select_fav.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_fav_lleno_icon).getConstantState())){
                        animesFavUpdate[0].remove(new Long(anime_previous.getMal_id()));
                        dbr.child(pref.getString("id","")).child("animes_fav").setValue(animesFavUpdate[0]);
                    }else{
                        animesFavUpdate[0].add(anime_previous.getMal_id());
                        dbr.child(pref.getString("id","")).child("animes_fav").setValue(animesFavUpdate[0]);
                    }
                }
            });
        }catch (Exception e){

        }

    }

    public void loadTabs(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabs.addTab(tabs.newTab().setText(getString(R.string.descripcion_tab)));
                if(!CenterActivity.prueba.equals("T1")){
                    tabs.addTab(tabs.newTab().setText(getString(R.string.episodios)));
                }
                tabs.addTab(tabs.newTab().setText(getString(R.string.personajes)));
                tabs.addTab(tabs.newTab().setText(getString(R.string.galeria)));
                //if(CenterActivity.login){
                tabs.addTab(tabs.newTab().setText(getString(R.string.comentarios)));
                //}
            }
        },200);
    }

    public void loadAnime(){
        API_SERVICE = ApiClientData.getClient().create(ApiAnimeData.class);
        Call<AnimeResource> call = API_SERVICE.getAnime(anime_previous.getMal_id());
        call.enqueue(new Callback<AnimeResource>() {
            @Override
            public void onResponse(Call<AnimeResource> call, Response<AnimeResource> response) {
                if(response.isSuccessful()){
                    anime = response.body();
                    change_data = true;
                    loadDataAnime();
                    traducir();
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadAnime();
                        }
                    },1000);
                }
            }

            @Override
            public void onFailure(Call<AnimeResource> call, Throwable t) {

            }
        });
    }

    public void loadDataAnime(){
        loadAiring(anime.isAiring());
        loadStars(anime.getScore());
        tv_year.setText(String.valueOf(anime.getAired().getProp().getFrom().getYear()));
        String generos = "";
        for (int i = 0; i < anime.getGenres().size(); i++) {
            for (int j = 0; j < CenterActivity.generos.size(); j++) {
                if(CenterActivity.generos.get(j).getMal_id() == anime.getGenres().get(i).getMal_id()){
                    if(i==anime.getGenres().size()-1){
                        generos = generos + CenterActivity.generos.get(j).getName() + ".";
                    }else{
                        generos = generos + CenterActivity.generos.get(j).getName() + ", ";
                    }
                }
            }
        }
        tv_generos.setText(generos);
        String url_trailer = "";
        if(anime.getTrailer_url()!=null){
            url_trailer = anime.getTrailer_url().split("embed/")[1].split("\\?")[0];
            getLifecycle().addObserver(yt_trailer);
            String finalUrl_trailer = url_trailer;
            yt_trailer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.cueVideo(finalUrl_trailer,0);
                }
            });
        }
    }

    public void traducir(){
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
            @Override
            public void onSuccess(Void unused) {
                translator.translate(anime.getSynopsis()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String desc = s.split("\\[")[0];
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //descF.loadDesc(desc);
                                Bundle bundle = new Bundle();
                                bundle.putString("desc",desc);
                                bundle.putBoolean("state",true);
                                try {
                                    descF.setArguments(bundle);
                                }catch (Exception e){
                                }
                                load_desc = true;
                            }
                        },800);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("TRADUCTOR","fallo");
            }
        });
    }

    public void loadFragments(){
        descF = new DescripcionFragment();
        fragments.add(descF);
        if(!CenterActivity.prueba.equals("T1")){
            Bundle bundle = new Bundle();
            bundle.putInt("ep",anime.getEpisodes());
            EpisodiosFragment episodiosFragment = new EpisodiosFragment();
            episodiosFragment.setArguments(bundle);
            fragments.add(episodiosFragment);
        }
        fragments.add(new PersonajesFragment());
        fragments.add(new GaleriaFragment());
        //if(CenterActivity.login){
            fragments.add(new ComentariosFragment());
        //}
        AdapterPager adapter = new AdapterPager(getSupportFragmentManager(),tabs.getTabCount(),fragments);
        vp_anime.setAdapter(adapter);
        vp_anime.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_anime));
    }

    public void loadAiring(boolean airing) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        Date today = new Date();
        try {
            myDate = formatter.parse(anime.getAired().getProp().getFrom().getDay()+"-"+anime.getAired().getProp().getFrom().getMonth()+"-"+anime.getAired().getProp().getFrom().getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(airing){
            tv_en_emision.setVisibility(View.VISIBLE);
            tv_finalizado.setVisibility(View.INVISIBLE);
        }else{
            tv_en_emision.setVisibility(View.INVISIBLE);
            tv_finalizado.setVisibility(View.VISIBLE);
            if(today.before(myDate)){
                long dif = myDate.getTime() - today.getTime();
                long difference_In_Days
                        = (dif
                        / (1000 * 60 * 60 * 24))
                        % 365;
                tv_en_emision.setText("Se estrena en " + difference_In_Days + " días");
                tv_en_emision.setVisibility(View.VISIBLE);
                tv_finalizado.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void loadStars(Float stars){
        tv_stars.setText(String.valueOf(stars));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}