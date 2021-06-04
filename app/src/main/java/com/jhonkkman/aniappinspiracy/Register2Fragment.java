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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhonkkman.aniappinspiracy.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Register2Fragment extends Fragment {

    private TextView tv_back;
    private NavController nav;
    private AppCompatButton btn_register;
    private FirebaseAuth mauth;
    private FirebaseUser user;
    private EditText et_nu;
    private CheckBox cb_edad;
    private DatabaseReference dbr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register2, container, false);
        tv_back = view.findViewById(R.id.tv_back);
        btn_register = view.findViewById(R.id.btn_register_register2);
        et_nu = view.findViewById(R.id.et_nombre_usuario_register);
        cb_edad = view.findViewById(R.id.cb_edad);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        mauth = FirebaseAuth.getInstance();
        onBack();
        onRegister();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
    }

    public void onRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = getArguments().getString("correo");
                String contra = getArguments().getString("contra");
                String nombre_usuario = et_nu.getText().toString();
                if(nombre_usuario.isEmpty()){
                    Toast.makeText(getContext(), "Ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
                }else{
                    if(nombre_usuario.length()>=6){
                        if(cb_edad.isChecked()){
                            AlertLoading dialog = new AlertLoading();
                            dialog.showDialog(getActivity(),getString(R.string.cargando));
                            mauth.createUserWithEmailAndPassword(correo,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        user = mauth.getCurrentUser();
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    List<Integer> listaf = new ArrayList<>();
                                                    List<Integer> listagf = new ArrayList<>();
                                                    List<Integer> listalav = new ArrayList<>();
                                                    Toast.makeText(getContext(), "Registro completado, se ha enviado un enlace de verificacion a tu correo electronico", Toast.LENGTH_LONG).show();
                                                    dbr.push().setValue(new User(nombre_usuario,"","https://cdn.myanimelist.net/images/anime/1900/110097.jpg",correo,listaf,listagf,listalav,false));
                                                    getActivity().finish();
                                                }else{
                                                    Toast.makeText(getContext(), "Error al completar registro, intentalo mas tarde", Toast.LENGTH_SHORT).show();
                                                }
                                                dialog.dismissDialog();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getContext(), "Error al completar registro, intentalo mas tarde", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismissDialog();
                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "Necesitas ser mayor de 16 años para utilizar la app", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "El nombre de usuario debe contener minimo 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onBack(){
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}