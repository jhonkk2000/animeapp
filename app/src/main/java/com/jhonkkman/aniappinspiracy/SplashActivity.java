package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;

import org.jetbrains.annotations.NotNull;

import static com.jhonkkman.aniappinspiracy.CenterActivity.generos;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference dbr;
    private ImageView iv_splah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbr = FirebaseDatabase.getInstance().getReference();
        iv_splah = findViewById(R.id.iv_splash);
        loadAnimationImage();
        if (NetworkUtils.isNetworkConnected(this)) {
            finishSplash();
        } else {
            Toast.makeText(this, "Verifica tu conexion a internet!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void loadAnimationImage() {
        RotateAnimation anim = new RotateAnimation(0f, 350f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        iv_splah.startAnimation(anim);
    }

    public void finishSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.child("genres").getChildren()) {
                            generos.add(ds.getValue(GeneroItem.class));
                        }
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            CenterActivity.season = snapshot.child("season").child("nombre").getValue().toString();
                            CenterActivity.year = Integer.parseInt(snapshot.child("season").child("year").getValue().toString());
                            startActivity(new Intent(SplashActivity.this, CenterActivity.class));
                        }else{
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        }, 1000);
    }
}