package com.jhonkkman.aniappinspiracy.data.models;

import java.io.Serializable;

public class AnimeItem implements Serializable {

    private int mal_id;
    private String title;
    private String image_url;
    private String sypnosis;
    private String type;
    private String airing_start;
    private int episodes;
    private String source;
    private float score;

    public AnimeItem(){

    }

    public AnimeItem(int mal_id, String title, String image_url) {
        this.mal_id = mal_id;
        this.title = title;
        this.image_url = image_url;
    }

    public int getMal_id() {
        return mal_id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSypnosis() {
        return sypnosis;
    }

    public String getType() {
        return type;
    }

    public String getAiring_start() {
        return airing_start;
    }

    public int getEpisodes() {
        return episodes;
    }

    public String getSource() {
        return source;
    }

    public float getScore() {
        return score;
    }
}
