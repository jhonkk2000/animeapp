package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    private NavController nav;
    private AppCompatButton btn_register,btn_login;
    private TextView tv_recover;
    private EditText et_corre,et_contra;
    private FirebaseAuth mauth;
    private DatabaseReference dbr;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btn_register = view.findViewById(R.id.btn_register_login);
        btn_login = view.findViewById(R.id.btn_login_login);
        tv_recover = view.findViewById(R.id.tv_recover);
        et_contra = view.findViewById(R.id.et_contra_login);
        et_corre = view.findViewById(R.id.et_correo_login);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        mauth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference("users");
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
                String correo = et_corre.getText().toString();
                String contra = et_contra.getText().toString();
                if(correo.isEmpty()||contra.isEmpty()){
                    Toast.makeText(getContext(), "Rellene ambas casillas", Toast.LENGTH_SHORT).show();
                }else{
                    if(verifyEmail(correo)){
                        AlertLoading dialog = new AlertLoading();
                        dialog.showDialog(getActivity(),"Iniciando Sesion");
                        mauth.signInWithEmailAndPassword(correo,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                User usuario = null;
                                                String id = "";
                                                boolean first = false;
                                                for (DataSnapshot ds : snapshot.getChildren()){
                                                    if(ds.child("correo").getValue().toString().equals(correo)){
                                                        usuario = ds.getValue(User.class);
                                                        first =(boolean) ds.child("first_time").getValue();
                                                        id = ds.getKey();
                                                        break;
                                                    }
                                                }
                                                if(usuario != null){
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(usuario);
                                                    editor.putString("usuario",json);
                                                    editor.putString("id",id);
                                                    editor.apply();
                                                    CenterActivity.login = true;
                                                    Toast.makeText(getContext(), "Se inicio sesion correctamente ", Toast.LENGTH_SHORT).show();
                                                    if(first){
                                                        startActivity(new Intent(getContext(),AnimeFavActivity.class));
                                                    }else{
                                                        startActivity(new Intent(getContext(),SplashActivity.class));
                                                        //dbr.child(id).child("firs_time").setValue(false);
                                                    }
                                                    getActivity().finish();
                                                    MainActivity.activityMain.finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }else{
                                    Toast.makeText(getContext(), "Error al iniciar sesion, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
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