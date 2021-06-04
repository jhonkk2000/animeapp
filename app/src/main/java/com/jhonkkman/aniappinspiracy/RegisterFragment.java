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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private AppCompatButton btn_register,btn_login;
    private NavController nav;
    private EditText et_corre,et_contra,et_recontra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        btn_register = view.findViewById(R.id.btn_register_register);
        btn_login = view.findViewById(R.id.btn_login_register);
        et_contra = view.findViewById(R.id.et_contra_register);
        et_corre = view.findViewById(R.id.et_correo_register);
        et_recontra = view.findViewById(R.id.et_recontra_register);
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

    public boolean verifyEmail(String email){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public void onRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_corre.getText().toString();
                String contra = et_contra.getText().toString();
                String recontra = et_recontra.getText().toString();
                if(correo.isEmpty() || contra.isEmpty() || recontra.isEmpty()){
                    Toast.makeText(getContext(), "Rellena todas las casillas", Toast.LENGTH_SHORT).show();
                }else{
                    if(verifyEmail(correo)){
                        if(contra.length()>=8){
                            if (contra.equals(recontra)){
                                Bundle bundle = new Bundle();
                                bundle.putString("correo",correo);
                                bundle.putString("contra",contra);
                                nav.navigate(R.id.action_registerFragment_to_register2Fragment,bundle);
                            }else{
                                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "La contraseña debe contener minimo 8 caracteres", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getContext(), "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
    }


}