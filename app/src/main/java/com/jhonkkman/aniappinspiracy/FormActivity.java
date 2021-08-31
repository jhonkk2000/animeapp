package com.jhonkkman.aniappinspiracy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class FormActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private NavController nav_controller ;
    private ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.light_red));
        btn_back = findViewById(R.id.btn_back_form);
        iv_image = findViewById(R.id.iv_form_image);
        nav_controller = Navigation.findNavController(this,R.id.fragment_form);
        if(getIntent().getBooleanExtra("register",false)){
            nav_controller.navigateUp();
            nav_controller.navigate(R.id.action_loginFragment_to_registerFragment);
        }
        loadImages();
        onBackButton();
    }

    public void onBackButton(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public void loadImages(){
        nav_controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                String label = destination.getLabel().toString();
                Bitmap new_image;
                if(label.equals("fragment_register")){
                    new_image = BitmapFactory.decodeResource(getResources(), R.drawable.image_register);
                    ImageViewAnimatedChange(FormActivity.this,iv_image,new_image);
                }else{
                    if(label.equals("fragment_login")){
                        new_image = BitmapFactory.decodeResource(getResources(), R.drawable.form_image);
                        ImageViewAnimatedChange(FormActivity.this,iv_image,new_image);
                    }else{
                        if(label.equals("fragment_register2")){
                            new_image = BitmapFactory.decodeResource(getResources(), R.drawable.image_register_two);
                            ImageViewAnimatedChange(FormActivity.this,iv_image,new_image);
                        }else{
                            new_image = BitmapFactory.decodeResource(getResources(), R.drawable.image_recover);
                            ImageViewAnimatedChange(FormActivity.this,iv_image,new_image);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}