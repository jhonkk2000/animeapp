package com.jhonkkman.aniappinspiracy.data.api;


import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ApiVideoServer {

    private String BASE_URL = "https://jkanime.net/";
    private String ANIME_NAME;
    private int EPISODE;

    public ApiVideoServer(String ANIME_NAME,int EPISODE){
        this.ANIME_NAME = ANIME_NAME;
        this.EPISODE = EPISODE;
    }

    public ArrayList<String> getVideoServers() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL oracle = new URL(BASE_URL+ANIME_NAME+"/"+EPISODE);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        String inputLine;
        ArrayList<String> datos = new ArrayList<>();
        ArrayList<String> url_videos = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
            datos.add(inputLine);
        }
        in.close();
        int v = 2;
        for (int i = 0; i < datos.size(); i++) {
            if(datos.get(i).trim().startsWith("video["+v+"] =")){
                if(datos.get(i).trim().split("src=\"")[1].startsWith("https://mega.nz")){
                    url_videos.add(datos.get(i).trim().split("src=\"")[1].split("\" scrolling")[0]);
                }else{
                    url_videos.add(datos.get(i).trim().split("src=\"")[1].split("\" width")[0]);
                }
                v++;
            }
        }
        return url_videos;
    }
}
