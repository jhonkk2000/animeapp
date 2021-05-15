package com.jhonkkman.aniappinspiracy.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnimeResource {

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }

}
