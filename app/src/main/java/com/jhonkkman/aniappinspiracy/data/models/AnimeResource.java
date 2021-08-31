package com.jhonkkman.aniappinspiracy.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AnimeResource {

    private int mal_id,episodes;
    private String image_url,trailer_url,title,type,source,status,rating,synopsis="",url;
    private Aired aired;
    private boolean airing;
    private float score;
    private List<GeneroItem> genres;

    public AnimeResource(){

    }

    public AnimeResource(int mal_id, int episodes, String image_url, String title, String type, String source, String synopsis, String url, boolean airing, float score, List<GeneroItem> genres) {
        this.mal_id = mal_id;
        this.episodes = episodes;
        this.image_url = image_url;
        this.title = title;
        this.type = type;
        this.source = source;
        this.synopsis = synopsis;
        this.url = url;
        this.airing = airing;
        this.score = score;
        this.genres = genres;
    }

    public String getUrl() {
        return url;
    }

    public Aired getAired() {
        return aired;
    }

    public int getMal_id() {
        return mal_id;
    }

    public int getEpisodes() {
        return episodes;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public String getRating() {
        return rating;
    }

    public String getSynopsis() {
        if(synopsis!= null){
            return synopsis;
        }else{
            return "";
        }
    }

    public boolean isAiring() {
        return airing;
    }

    public float getScore() {
        return score;
    }

    public List<GeneroItem> getGenres() {
        return genres;
    }
}
