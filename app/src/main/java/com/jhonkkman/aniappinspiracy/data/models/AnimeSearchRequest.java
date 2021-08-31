package com.jhonkkman.aniappinspiracy.data.models;

import java.util.List;

public class AnimeSearchRequest {

    private int status = 0;

    public int getStatus() {
        return status;
    }

    private List<AnimeItem> results;

    public List<AnimeItem> getAnimeItems() {
        return results;
    }
}
