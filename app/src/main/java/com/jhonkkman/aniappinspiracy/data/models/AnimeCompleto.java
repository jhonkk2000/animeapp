package com.jhonkkman.aniappinspiracy.data.models;

import java.util.ArrayList;

public class AnimeCompleto {

    private AnimeResource anime;
    private ArrayList<Episodio> episodios;

    public AnimeCompleto(AnimeResource anime, ArrayList<Episodio> episodios) {
        this.anime = anime;
        this.episodios = episodios;
    }

    public AnimeResource getAnime() {
        return anime;
    }

    public ArrayList<Episodio> getEpisodios() {
        return episodios;
    }

    public void setAnime(AnimeResource anime) {
        this.anime = anime;
    }

    public void setEpisodios(ArrayList<Episodio> episodios) {
        this.episodios = episodios;
    }
}
