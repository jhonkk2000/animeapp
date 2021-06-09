package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nombre_usuario,descripcion,url_foto,correo;
    private List<Integer> anime_fav = new ArrayList<>();
    private List<Integer> genero_fav = new ArrayList<>();
    private List<Integer> last_anime_view = new ArrayList<>();
    private boolean first_time;

    public User(){

    }

    public User(String nombre_usuario, String descripcion, String url_foto, String correo, List<Integer> anime_fav, List<Integer> genero_fav, List<Integer> last_anime_view, boolean firs_time) {
        this.nombre_usuario = nombre_usuario;
        this.descripcion = descripcion;
        this.url_foto = url_foto;
        this.correo = correo;
        this.anime_fav = anime_fav;
        this.genero_fav = genero_fav;
        this.last_anime_view = last_anime_view;
        this.first_time = firs_time;
    }

    public List<Integer> getAnime_fav() {
        return anime_fav;
    }

    public List<Integer> getGenero_fav() {
        return genero_fav;
    }

    public List<Integer> getLast_anime_view() {
        return last_anime_view;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isFirs_time() {
        return first_time;
    }


}