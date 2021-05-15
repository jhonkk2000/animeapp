package com.jhonkkman.aniappinspiracy.data.api;

import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiAnimeData {

    @GET("anime/42361")
    Call<AnimeResource> getAnime();

    @GET("season/2021/winter")
    Call<AnimeTopResource> getAnimeTop();

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeSearch(@Query("q") String name,@Query("page") int page);

}
