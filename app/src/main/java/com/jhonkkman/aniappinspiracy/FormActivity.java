package com.jhonkkman.aniappinspiracy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private NavController nav_controller ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        btn_back = findViewById(R.id.btn_back_form);
        if(getIntent().getBooleanExtra("register",false)){
            nav_controller = Navigation.findNavController(this,R.id.fragment_form);
            nav_controller.navigateUp();
            nav_controller.navigate(R.id.action_loginFragment_to_registerFragment);
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}