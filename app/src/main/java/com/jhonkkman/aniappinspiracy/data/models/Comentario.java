package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class Comentario {

    private String comentario,user,num;
    private ArrayList<String> likes = new ArrayList<>();
    private ArrayList<String> dislikes = new ArrayList<>();

    public Comentario(){

    }

    public Comentario(String comentario, String user, ArrayList<String> likes, ArrayList<String> dislikes) {
        this.comentario = comentario;
        this.user = user;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getComentario() {
        return comentario;
    }

    public String getUser() {
        return user;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public ArrayList<String> getDislikes() {
        return dislikes;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
