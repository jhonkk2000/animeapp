package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {

    private ImageView iv_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        iv_imagen = findViewById(R.id.iv_full_image);
        loadImage();
    }

    public void loadImage(){
        Glide.with(this).load(getIntent().getStringExtra("url")).into(iv_imagen);
    }
}