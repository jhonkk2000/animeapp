package com.jhonkkman.aniappinspiracy.data.models;

import java.io.Serializable;

public class GeneroItem implements Serializable {

    private int mal_id;
    private String name,url;

    public int getMal_id() {
        return mal_id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
