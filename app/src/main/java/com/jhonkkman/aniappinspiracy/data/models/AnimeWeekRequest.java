package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class AnimeWeekRequest {
    private ArrayList<AnimeItem> monday;
    private ArrayList<AnimeItem> tuesday;
    private ArrayList<AnimeItem> wednesday;
    private ArrayList<AnimeItem> thursday;
    private ArrayList<AnimeItem> friday;
    private ArrayList<AnimeItem> saturday;
    private ArrayList<AnimeItem> sunday;

    public ArrayList<AnimeItem> getMonday() {
        return monday;
    }

    public ArrayList<AnimeItem> getTuesday() {
        return tuesday;
    }

    public ArrayList<AnimeItem> getWednesday() {
        return wednesday;
    }

    public ArrayList<AnimeItem> getThursday() {
        return thursday;
    }

    public ArrayList<AnimeItem> getFriday() {
        return friday;
    }

    public ArrayList<AnimeItem> getSaturday() {
        return saturday;
    }

    public ArrayList<AnimeItem> getSunday() {
        return sunday;
    }
}
