package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nombre_usuario,descripcion,url_foto,correo;
    private List<Integer> animes_fav = new ArrayList<>();
    private List<Integer> genero_fav = new ArrayList<>();
    private List<Integer> last_anime_view = new ArrayList<>();
    private boolean first_time;
    private ArrayList<AnimeItem> last_animes = new ArrayList<>();

    public User(){

    }

    public User(String nombre_usuario, String descripcion, String url_foto, String correo, List<Integer> animes_fav, List<Integer> genero_fav, List<Integer> last_anime_view, boolean first_time) {
        this.nombre_usuario = nombre_usuario;
        this.descripcion = descripcion;
        this.url_foto = url_foto;
        this.correo = correo;
        this.animes_fav = animes_fav;
        this.genero_fav = genero_fav;
        this.last_anime_view = last_anime_view;
        this.first_time = first_time;
    }

    public ArrayList<AnimeItem> getLast_animes() {
        return last_animes;
    }

    public List<Integer> getAnimes_fav() {
        return animes_fav;
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

    public boolean isFirst_time() {
        return first_time;
    }


}
