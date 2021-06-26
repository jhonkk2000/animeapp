package com.jhonkkman.aniappinspiracy.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.AdapterGaleria;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.R;
import com.jhonkkman.aniappinspiracy.data.models.User;
import com.jhonkkman.aniappinspiracy.ui.favorito.FavoritoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerfilFragment extends Fragment {

    private RecyclerView rv_change_img;
    private LinearLayoutManager lym;
    private AdapterGaleria adapter;
    private EditText et_nombre,et_desc;
    private ImageButton btn_nombre,btn_desc;
    private AppCompatButton btn_guardar;
    private SharedPreferences pref;
    private User user;
    private DatabaseReference dbr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        rv_change_img = view.findViewById(R.id.rv_change_img);
        btn_desc = view.findViewById(R.id.btn_cambiar_desc);
        et_desc = view.findViewById(R.id.et_desc_perfil);
        et_nombre = view.findViewById(R.id.et_nombre_usuario_perfil);
        btn_nombre = view.findViewById(R.id.btn_cambiar_usuario);
        btn_guardar = view.findViewById(R.id.btn_guardar_perfil);
        btn_guardar.setVisibility(View.INVISIBLE);
        pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        dbr = FirebaseDatabase.getInstance().getReference("users");
        //loadImg();
        loadUserData();
        editData();
        savedData();
        return view;
    }

    public void editData(){
        btn_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_nombre.setEnabled(true);
                et_nombre.requestFocus();
                et_desc.setText(user.getDescripcion());
                btn_guardar.setVisibility(View.VISIBLE);
                btn_nombre.setVisibility(View.INVISIBLE);
            }
        });
        btn_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_desc.setEnabled(true);
                et_desc.setText(user.getDescripcion());
                et_desc.requestFocus();
                btn_guardar.setVisibility(View.VISIBLE);
                btn_desc.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void savedData() {
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nu = et_nombre.getText().toString();
                String desc = et_desc.getText().toString();
                if(nu.isEmpty()){
                    Toast.makeText(getContext(), "No se permite un nombre de usuario vacio", Toast.LENGTH_SHORT).show();
                    et_nombre.setText(user.getNombre_usuario());
                    et_desc.setText("Descripcion: " + user.getDescripcion());
                }else{
                    dbr.child(pref.getString("id","")).child("nombre_usuario").setValue(nu);
                    dbr.child(pref.getString("id","")).child("descripcion").setValue(desc);
                    Toast.makeText(getContext(), "Cambios guardados correctamente!", Toast.LENGTH_SHORT).show();
                    et_nombre.setText(nu);
                    et_desc.setText("Descripcion: " + desc);
                }
                btn_guardar.setVisibility(View.INVISIBLE);
                btn_nombre.setVisibility(View.VISIBLE);
                btn_desc.setVisibility(View.VISIBLE);
                et_nombre.setEnabled(false);
                et_desc.setEnabled(false);
            }
        });
    }

    public void loadUserData(){
        Gson gson = new Gson();
        String json = pref.getString("usuario","");
        user = gson.fromJson(json, User.class);
        et_nombre.setText(user.getNombre_usuario());
        et_desc.setText("Descripcion: " + user.getDescripcion());
    }

    public void loadImg(){
        lym = new LinearLayoutManager(getContext());
        //adapter = new AdapterResultados();
        rv_change_img.setLayoutManager(lym);
        rv_change_img.setAdapter(adapter);
    }
}