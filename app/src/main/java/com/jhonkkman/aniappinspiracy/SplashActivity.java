package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.data.models.Aviso;
import com.jhonkkman.aniappinspiracy.data.models.Extra;
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;
import com.jhonkkman.aniappinspiracy.ui.inicio.InicioFragment;

import org.jetbrains.annotations.NotNull;

import static com.jhonkkman.aniappinspiracy.CenterActivity.avisos;
import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference dbr;
    private ImageView iv_splah;
    private SharedPreferences prefD;
    private FirebaseUser user;
    private int im = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.light_red));
        dbr = FirebaseDatabase.getInstance().getReference();
        iv_splah = findViewById(R.id.iv_splash);
        prefD = getSharedPreferences("darkMode", MODE_PRIVATE);
        loadDark();
        loadAnimationImage();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (NetworkUtils.isNetworkConnected(this)) {
            finishSplash();
        } else {
            Toast.makeText(this, "Verifica tu conexion a internet!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void loadDark() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void loadAnimationImage() {
        RotateAnimation anim = new RotateAnimation(0f, 350f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        iv_splah.startAnimation(anim);
    }

    public void finishSplash() {
        dbr.child("genres").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                generos.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    generos.add(ds.getValue(GeneroItem.class));
                }
                dbr.child("avisos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        avisos.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            avisos.add(ds.getValue(Aviso.class));
                            Log.d("PROBANDING", "1" + avisos.size());
                        }
                        dbr.child("extras").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                CenterActivity.extras = snapshot.getValue(Extra.class);
                                dbr.child("prueba").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (!getIntent().getBooleanExtra("register", true)) {
                                            startActivity(new Intent(SplashActivity.this, CenterActivity.class).putExtra("prueba", snapshot.child("nombre").getValue().toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            CenterActivity.login = false;
                                        } else {
                                            if (user != null) {
                                                startActivity(new Intent(SplashActivity.this, CenterActivity.class).putExtra("prueba", snapshot.child("nombre").getValue().toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                CenterActivity.login = true;
                                            } else {
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            }
                                        }
                                        //CenterActivity.prueba = snapshot.child("prueba").child("nombre").getValue().toString();
                                        //CenterActivity.season = snapshot.child("season").child("nombre").getValue().toString();
                                        //CenterActivity.year = Integer.parseInt(snapshot.child("season").child("year").getValue().toString());
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}