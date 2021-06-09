package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class Episodio {

    private ArrayList<String> videos;

    public Episodio(ArrayList<String> videos) {
        this.videos = videos;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }
}
