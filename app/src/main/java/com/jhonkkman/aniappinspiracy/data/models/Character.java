package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class Character {

    private int mall_id;
    private String image_url,name;
    private ArrayList<VoiceActor> voice_actors;



    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public ArrayList<VoiceActor> getVoice_actors() {
        return voice_actors;
    }
}
