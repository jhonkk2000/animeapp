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
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecoverFragment extends Fragment {

    private AppCompatButton btn_send, btn_login, btn_register;
    private NavController nav;
    private EditText et_corre;
    private FirebaseAuth mauth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recover, container, false);
        btn_login = view.findViewById(R.id.btn_login_recover);
        btn_register = view.findViewById(R.id.btn_register_recover);
        btn_send = view.findViewById(R.id.btn_send_recover);
        et_corre = view.findViewById(R.id.et_correo_recover);
        mauth = FirebaseAuth.getInstance();
        onSend();
        onLogin();
        onRegister();

        return view;
    }

    public void onSend(){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_corre.getText().toString();
                if (correo.isEmpty()){
                    Toast.makeText(getContext(), "Ingresa un correo electronico", Toast.LENGTH_SHORT).show();
                }else{
                    if(verifyEmail(correo)){
                        AlertLoading dialog = new AlertLoading();
                        dialog.showDialog(getActivity(),getString(R.string.recover_cargando));
                        mauth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Se ha enviado un enlace de restablecimiento de contraseña a tu correo ingresado", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(), "No se pudo generar la solicitud, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismissDialog();
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
                    }
                }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
    }

    public void onLogin(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_recoverFragment_to_loginFragment);
            }
        });
    }

    public void onRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_recoverFragment_to_registerFragment);
            }
        });
    }
}