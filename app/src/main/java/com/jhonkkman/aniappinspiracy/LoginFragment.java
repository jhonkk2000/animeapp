package com.jhonkkman.aniappinspiracy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LoginFragment extends Fragment {

    private NavController nav;
    private AppCompatButton btn_register,btn_login;
    private TextView tv_recover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btn_register = view.findViewById(R.id.btn_register_login);
        btn_login = view.findViewById(R.id.btn_login_login);
        tv_recover = view.findViewById(R.id.tv_recover);
        onRegisterButton();
        onRecover();
        onLoginButton();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
    }

    public void onLoginButton(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AnimeFavActivity.class));
            }
        });
    }

    public void onRegisterButton(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_loginFragment_to_registerFragment2);
            }
        });
    }

    public void onRecover(){
        tv_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_loginFragment_to_recoverFragment);
            }
        });
    }


}