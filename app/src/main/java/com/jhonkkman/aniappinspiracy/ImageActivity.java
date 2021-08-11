package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    private ImageView iv_imagen;
    private AppCompatButton btn_set;
    private AlertOptions dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        iv_imagen = findViewById(R.id.iv_full_image);
        btn_set = findViewById(R.id.btn_set_wallpaper);
        dialog = new AlertOptions();
        loadImage();
        setWallpaper();
    }

    public void setWallpaper(){
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog(ImageActivity.this);
                dialog.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface d) {
                        if(dialog.send){
                            iv_imagen.buildDrawingCache();
                            Bitmap bit = iv_imagen.getDrawingCache();
                            WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
                            try {
                                wallManager.setBitmap(bit);
                                Toast.makeText(ImageActivity.this, "Se establecio como fondo de pantalla", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }

    public void loadImage(){
        Glide.with(this).load(getIntent().getStringExtra("url")).into(iv_imagen);
    }
}