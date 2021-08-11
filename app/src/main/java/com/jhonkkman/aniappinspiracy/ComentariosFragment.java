package com.jhonkkman.aniappinspiracy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jhonkkman.aniappinspiracy.data.models.AnimeComentarios;
import com.jhonkkman.aniappinspiracy.data.models.Comentario;
import com.jhonkkman.aniappinspiracy.data.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ComentariosFragment extends Fragment {

    private RecyclerView rv_c;
    private LinearLayoutManager lym;
    private AdapterComentario adapter;
    private ImageButton btn_enviar;
    private EditText et_comentario;
    private DatabaseReference dbr;
    private ArrayList<Comentario> comentarios = new ArrayList<>();
    private SharedPreferences pref;
    public static User user;
    private String KEY_COMENTARIO = "";
    private boolean comment = false;
    private ArrayList<User> users = new ArrayList<>();
    private Spinner sp_filtro;
    private LinearLayout ly_nodata;
    private ArrayList<Comentario> comentarios_filter = new ArrayList<>();
    private boolean first_time = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);
        rv_c = view.findViewById(R.id.rv_comentarios);
        ly_nodata = view.findViewById(R.id.ly_nodata_comentarios);
        btn_enviar = view.findViewById(R.id.btn_enviar_comentario);
        btn_enviar.setEnabled(false);
        et_comentario = view.findViewById(R.id.et_comentario);
        dbr = FirebaseDatabase.getInstance().getReference();
        sp_filtro = view.findViewById(R.id.sp_comentarios);
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        //loadSpinner();
        loadKey();
        loadUser();
        loadUsers();
        //loadComentarios();
        //sendComment();
        //loadListComments();
        return view;
    }

    public void loadSpinner() {
        sp_filtro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderList(i);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void orderList(int i) {
        comentarios_filter = comentarios;
        if (i == 1) {
            Collections.sort(comentarios_filter, new Comparator<Comentario>() {
                @Override
                public int compare(Comentario comentario, Comentario t1) {
                    return new Integer(t1.getLikes().size()).compareTo(new Integer(comentario.getLikes().size()));
                }
            });
        } else {
            Collections.sort(comentarios_filter, new Comparator<Comentario>() {
                @Override
                public int compare(Comentario comentario, Comentario t1) {
                    return new Integer(t1.getNum()).compareTo(new Integer(comentario.getNum()));
                }
            });
        }
    }

    public void loadUsers() {
        dbr.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        users.add(ds.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void verifyComment() {
        if (this.isAdded()) {
            if (comment) {
                btn_enviar.setEnabled(false);
                et_comentario.setEnabled(false);
                btn_enviar.setBackground(getContext().getDrawable(R.drawable.background_disable_minibutton));
            } else {
                btn_enviar.setEnabled(true);
                btn_enviar.setBackground(getContext().getDrawable(R.drawable.background_mini_button));
            }
        }
    }

    public void loadKey() {
        dbr.child("anime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (Integer.parseInt(ds.child("mall_id").getValue().toString()) == AnimeActivity.anime_previous.getMal_id()) {
                            KEY_COMENTARIO = ds.getKey();
                        }
                    }
                }
                loadComentarios();
                loadListComments();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void loadListComments() {
        dbr.child("anime").child(KEY_COMENTARIO).child("comentarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                comentarios.clear();
                if (snapshot.exists()) {
                    ArrayList<String> nums = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        comentarios.add(ds.getValue(Comentario.class));
                        nums.add(ds.getKey());
                    }
                    for (int i = 0; i < comentarios.size(); i++) {
                        comentarios.get(i).setNum(nums.get(i));
                        if (comentarios.get(i).getUser().equals(user.getCorreo())) {
                            comment = true;
                        }
                    }
                    ly_nodata.setVisibility(View.INVISIBLE);
                    rv_c.setVisibility(View.VISIBLE);
                }
                loadSpinner();
                verifyComment();
                sendComment();
                orderList(sp_filtro.getSelectedItemPosition());
                if (!first_time) {
                    loadComentarios();
                    first_time = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void loadUser() {
        Gson gson = new Gson();
        String json = pref.getString("usuario", "");
        user = gson.fromJson(json, User.class);
        if (!CenterActivity.login) {
            btn_enviar.getLayoutParams().height = 0;
            et_comentario.getLayoutParams().height = 0;
            btn_enviar.requestLayout();
            et_comentario.requestLayout();
        }
    }

    public void sendComment() {
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comentario = et_comentario.getText().toString();
                if (!comentario.isEmpty()) {
                    ArrayList<String> likes = new ArrayList<>();
                    ArrayList<String> dislikes = new ArrayList<>();
                    comentarios.add(new Comentario(comentario, user.getCorreo(), likes, dislikes));
                    if (KEY_COMENTARIO.isEmpty()) {
                        dbr.child("anime").push().setValue(new AnimeComentarios(AnimeActivity.anime_previous.getMal_id(), comentarios));
                    } else {
                        dbr.child("anime").child(KEY_COMENTARIO).child("comentarios").setValue(comentarios);
                    }
                    et_comentario.setText("");
                    loadKey();
                } else {
                    Toast.makeText(getContext(), "Ingresa un comentario", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void loadComentarios() {
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterComentario(getContext(), comentarios_filter, users, dbr, KEY_COMENTARIO);
        rv_c.setLayoutManager(lym);
        rv_c.setAdapter(adapter);
    }


}