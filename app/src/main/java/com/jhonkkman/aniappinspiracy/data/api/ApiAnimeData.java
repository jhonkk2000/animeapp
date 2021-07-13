package com.jhonkkman.aniappinspiracy.data.api;

import com.jhonkkman.aniappinspiracy.data.models.AnimeGenResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeSearchRequest;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeTopSeasonResource;
import com.jhonkkman.aniappinspiracy.data.models.AnimeWeekRequest;
import com.jhonkkman.aniappinspiracy.data.models.GaleriaResource;
import com.jhonkkman.aniappinspiracy.data.models.PersonajesResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiAnimeData {

    @GET("genre/anime/{id}")
    Call<AnimeGenResource> getGeneroAnime(@Path("id")int id);

    @GET("anime/{id}")
    Call<AnimeResource> getAnime(@Path("id")int id);

    @GET("anime/{id}/characters_staff")
    Call<PersonajesResource> getPersonajes(@Path("id")int id);

    @GET("anime/{id}/pictures")
    Call<GaleriaResource> getGaleria(@Path("id")int id);

    @GET("season/{year}/{season}")
    Call<AnimeTopSeasonResource> getAnimeTopSeason(@Path("year") int year,@Path("season") String season);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeSearch(@Query("q") String name);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeLetter(@Query("letter") String name,@Query("order_by") String order,@Query("sort") String sort);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeType(@Query("type") String name,@Query("order_by") String order,@Query("sort") String sort);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeRated(@Query("rated") String name,@Query("order_by") String order,@Query("sort") String sort);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeScore(@Query("score") float name);

    @GET("search/anime")
    Call<AnimeSearchRequest> getAnimeGenre(@Query("genre") int name,@Query("order_by") String order,@Query("sort") String sort);

    @GET("top/anime")
    Call<AnimeTopResource> getAnimeTop();

    @GET("schedule")
    Call<AnimeWeekRequest> getAnimeWeek();

    @GET("top/anime/1/{subtype}")
    Call<AnimeTopResource> getAnimeTopSubtype(@Path("subtype") String subtype);

}
