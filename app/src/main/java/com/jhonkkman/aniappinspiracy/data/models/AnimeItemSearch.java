package com.jhonkkman.aniappinspiracy.data.models;

public class AnimeItemSearch {

    private int mal_id;
    private String title;
    private String image_url;
    private String sypnosis;
    private String type;
    private boolean airing;
    private int episodes;
    private float score;

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

    public boolean isAiring() {
        return airing;
    }

    public int getEpisodes() {
        return episodes;
    }

    public float getScore() {
        return score;
    }
}
