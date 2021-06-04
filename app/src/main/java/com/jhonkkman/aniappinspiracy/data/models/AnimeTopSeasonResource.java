package com.jhonkkman.aniappinspiracy.data.models;

import java.util.List;

public class AnimeTopSeasonResource {

    private String season_name;
    private int season_year;
    private List<AnimeItem> anime;

    public String getSeason_name() {
        return season_name;
    }

    public int getSeason_year() {
        return season_year;
    }

    public List<AnimeItem> getAnime() {
        return anime;
    }
}
