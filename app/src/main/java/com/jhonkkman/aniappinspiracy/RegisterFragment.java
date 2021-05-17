package com.jhonkkman.aniappinspiracy;

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

public class RegisterFragment extends Fragment {

    private AppCompatButton btn_register,btn_login;
    private NavController nav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        btn_register = view.findViewById(R.id.btn_register_register);
        btn_login = view.findViewById(R.id.btn_login_register);

        onRegister();
        onLogin();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
    }

    public void onLogin(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_registerFragment_to_loginFragment);
                //closeFragment();
            }
        });
    }

    public void onRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_registerFragment_to_register2Fragment);
            }
        });
    }

    public void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();

    }


}