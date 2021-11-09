package com.jhonkkman.aniappinspiracy.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AnimeItem implements Serializable {

    private int mal_id;
    private String title;
    private String image_url;
    private String url;
    private String synopsis;
    private String type;
    private String start_date;
    private String rated;
    private boolean airing;
    private ArrayList<GeneroItem> genres = new ArrayList<>();
    private ArrayList<GeneroItem> themes = new ArrayList<>();
    private ArrayList<GeneroItem> demographics = new ArrayList<>();

    @SerializedName("episodes")
    private int episodes;

    private String source;
    private float score;
    private boolean kids;
    private String airing_start;

    public AnimeItem() {

    }

    public AnimeItem(int mal_id, String title, String image_url) {
        this.mal_id = mal_id;
        this.title = title;
        this.image_url = image_url;
    }

    public boolean isKids() {
        return kids;
    }

    public String getAiring_start() {
        return airing_start;
    }

    public String getRated() {
        if (rated != null) {
            return rated;
        } else {
            return "Rx";
        }
    }

    public ArrayList<GeneroItem> getGenres() {
        ArrayList<GeneroItem> allgenres = new ArrayList<>();
        for (int i = 0; i < genres.size(); i++) {
            allgenres.add(genres.get(0));
        }
        for (int i = 0; i < themes.size(); i++) {
            allgenres.add(themes.get(0));
        }
        for (int i = 0; i < demographics.size(); i++) {
            allgenres.add(demographics.get(0));
        }
        if(allgenres.size()==0){
            GeneroItem generoItem = new GeneroItem();
            generoItem.setMal_id(12);
            generoItem.setName("Desconocido");
            allgenres.add(generoItem);
        }
        return allgenres;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
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

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
