package com.jhonkkman.aniappinspiracy.data.models;

public class AnimeItem {

    private int mal_id;
    private String title;
    private String image_url;
    private String sypnosis;
    private String type;
    private String airing_start;
    private int episodes;
    private String source;
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
