package com.jhonkkman.aniappinspiracy.data.models;

public class ItemSearch {

    private String nombre,url_foto = "";
    private int mall_id;



    public ItemSearch(String nombre) {
        this.nombre = nombre;
    }

    public int getMall_id() {
        return mall_id;
    }

    public void setMall_id(int mall_id) {
        this.mall_id = mall_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }
}
