package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class AnimeComentarios {

    private int mall_id;
    private ArrayList<Comentario> comentarios = new ArrayList<>();

    public AnimeComentarios(){

    }

    public AnimeComentarios(int mall_id, ArrayList<Comentario> comentarios) {
        this.mall_id = mall_id;
        this.comentarios = comentarios;
    }

    public int getMall_id() {
        return mall_id;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }
}
