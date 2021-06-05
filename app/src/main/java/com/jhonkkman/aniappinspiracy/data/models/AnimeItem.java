package com.jhonkkman.aniappinspiracy.data.models;

import java.io.Serializable;

public class AnimeItem implements Serializable {

    private int mal_id;
    private String title;
    private String image_url;
    private String synopsis;
    private String type;
    private String start_date;
    private boolean airing;
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

    public String getSynopsis() {
        return synopsis;
    }

    public String getType() {
        return type;
    }

    public String getStart_date() {
        return start_date;
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

    public boolean isAiring() {
        return airing;
    }
}
