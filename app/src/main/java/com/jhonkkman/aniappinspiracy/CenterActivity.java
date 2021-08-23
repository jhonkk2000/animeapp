package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.models.AnimeCompleto;
import com.jhonkkman.aniappinspiracy.data.models.AnimeItem;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.Aviso;
import com.jhonkkman.aniappinspiracy.data.models.Extra;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.data.models.TopMemoria;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static List<GeneroItem> generos = new ArrayList<>();
    private DatabaseReference dbr;
    private SharedPreferences pref;
    public static User user = new User();
    private TextView tv_nombreu;
    private ImageView iv_user;
    private NavigationView navigationView;
    public static boolean login;
    public static ArrayList<AnimeItem> animesI = new ArrayList<>();
    public static ArrayList<ArrayList<AnimeItem>> animesG = new ArrayList<>();
    public static List<GeneroItem> generosG = new ArrayList<>();
    public static List<AnimeItem> animeItems = new ArrayList<>();
    public static ArrayList<AnimeCompleto> animesGuardados = new ArrayList<>();
    public static ArrayList<TopMemoria> animesTop = new ArrayList<>();
    public static ArrayList<AnimeResource> animesFav = new ArrayList<>();
    public static Extra extras = new Extra();;
    public static String season;
    public static String prueba;
    public static boolean carousel_state = false;
    public static ArrayList<Aviso> avisos = new ArrayList<>();
    private int finish_state = 0;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        dbr = FirebaseDatabase.getInstance().getReference();
        pref = getSharedPreferences("user", MODE_PRIVATE);
        prueba = getIntent().getStringExtra("prueba");
        loadUser();
        updateUserLocal();
        updateFav();
        loadMenuItems();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_animes, R.id.nav_comunidad, R.id.nav_configuracion, R.id.nav_acerca_de)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed() {
        if (AnimesFragment.pos_bototm_nav == 0) {
            if (finish_state == 0) {
                finish_state = 1;
                Toast.makeText(this, "Vuelve a pulsar para salir", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish_state = 0;
                    }
                }, 3000);
            } else {
                finish();
                System.exit(0);
            }

        } else {
            AnimesFragment.bnv.setSelectedItemId(R.id.nav_inicio);
        }

    }

    public void loadMenuItems() {
        Menu menu = navigationView.getMenu();
        MenuItem mi_config = menu.findItem(R.id.nav_configuracion);
        MenuItem mi_acerca = menu.findItem(R.id.nav_acerca_de);
        MenuItem mi_perfil = menu.findItem(R.id.nav_perfil);
        MenuItem mi_comunidad = menu.findItem(R.id.nav_comunidad);
        MenuItem mi_inicio = menu.findItem(R.id.nav_animes);
        mi_inicio.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
        mi_comunidad.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(CenterActivity.this, ComunidadActivity.class));
                return false;
            }
        });
        mi_config.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(CenterActivity.this, ContainerNavActivity.class);
                i.putExtra("fragment", "c");
                startActivity(i);
                return false;
            }
        });
        mi_acerca.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(CenterActivity.this, ContainerNavActivity.class);
                i.putExtra("fragment", "a");
                startActivity(i);
                return false;
            }
        });
        mi_perfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (CenterActivity.login) {
                    Intent i = new Intent(CenterActivity.this, ContainerNavActivity.class);
                    i.putExtra("fragment", "p");
                    startActivity(i);
                } else {
                    Toast.makeText(CenterActivity.this, "No haz iniciado sesión", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    public void loadDataNav() {
        View view = navigationView.getHeaderView(0);
        iv_user = view.findViewById(R.id.iv_foto_user);
        tv_nombreu = view.findViewById(R.id.tv_nombre_usuario_nav);
        if (!login) {
            tv_nombreu.setText("Anonimo");
        } else {
            tv_nombreu.setText(user.getNombre_usuario());
            Glide.with(getApplicationContext()).load(user.getUrl_foto()).into(iv_user);
        }
    }

    public void loadUser() {
        Gson gson = new Gson();
        String json = pref.getString("usuario", "");
        user = gson.fromJson(json, User.class);
        loadDataNav();
    }

    public void updateFav() {
        dbr.child("users").child(pref.getString("id", "")).child("animes_fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FavoritoFragment.animesFav = (ArrayList<Integer>) snapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void updateUserLocal() {
        dbr.child("users").child(pref.getString("id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User usuario = snapshot.getValue(User.class);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(usuario);
                    editor.putString("usuario", json);
                    editor.putString("id", snapshot.getKey());
                    editor.apply();
                    loadUser();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}