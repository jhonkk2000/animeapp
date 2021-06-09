package com.jhonkkman.aniappinspiracy.data.models;

public class TopMemoria {

    private AnimeTopResource top;
    private String subtype;

    public TopMemoria(AnimeTopResource top, String subtype) {
        this.top = top;
        this.subtype = subtype;
    }

    public AnimeTopResource getTop() {
        return top;
    }

    public String getSubtype() {
        return subtype;
    }
}
