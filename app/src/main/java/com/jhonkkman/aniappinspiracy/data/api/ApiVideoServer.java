package com.jhonkkman.aniappinspiracy.data.api;


import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ApiVideoServer {

    private String BASE_URL = "https://jkanime.net/";
    private String ANIME_NAME;
    private int EPISODE;

    public ApiVideoServer() {

    }

    public ApiVideoServer(String ANIME_NAME, int EPISODE) {
        this.ANIME_NAME = ANIME_NAME;
        this.EPISODE = EPISODE;
    }

    public ArrayList<String> getVideoServers() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (ANIME_NAME.contains("__")) {
            ANIME_NAME = ANIME_NAME.split("__")[0] + "-" + ANIME_NAME.split("__")[1];
        }
        String[] dat = ANIME_NAME.split("_");
        String anime_final = "";
        for (int i = 0; i < dat.length; i++) {
            if (i == dat.length - 1) {
                anime_final = anime_final + dat[i].toLowerCase();
            } else {
                anime_final = anime_final + dat[i].toLowerCase() + "-";
            }
        }
        URL oracle = new URL(BASE_URL + anime_final + "/" + EPISODE);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        String inputLine;
        ArrayList<String> datos = new ArrayList<>();
        ArrayList<String> url_videos = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            datos.add(inputLine);
        }
        in.close();
        int v = 1;
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).trim().startsWith("video[" + v + "] =")) {
                //Log.d("Videos:::::::", datos.get(i) + " cant: " + v);
                if (datos.get(i).trim().split("src=")[1].startsWith("https://mega.nz")) {
                    url_videos.add(datos.get(i).trim().split("src=")[1].split(" scrolling")[0].replace("\"", ""));
                } else {
                    url_videos.add(datos.get(i).trim().split("src=")[1].split(" width")[0].replace("\"", ""));
                }
                v++;
            }
        }
        return url_videos;
    }

    public int getCountEpisodes() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (ANIME_NAME.contains("__")) {
            ANIME_NAME = ANIME_NAME.split("__")[0] + "-" + ANIME_NAME.split("__")[1];
        }
        String[] dat = ANIME_NAME.split("_");
        String anime_final = "";
        for (int i = 0; i < dat.length; i++) {
            if (i == dat.length - 1) {
                anime_final = anime_final + dat[i].toLowerCase();
            } else {
                anime_final = anime_final + dat[i].toLowerCase() + "-";
            }
        }
        URL oracle = new URL(BASE_URL + anime_final);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        String inputLine;
        ArrayList<String> datos = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            datos.add(inputLine);
        }
        in.close();
        ArrayList<String> datos2 = new ArrayList<>();
        for (int i = 0; i < datos.size(); i++) {
            if(datos.get(i).contains("<a class=\"numbers\"")){
                Log.d("NUMBRES",datos.get(i) + "CAN");
                datos2.add(datos.get(i));
            }
        }
        int ep = 0;
        if(datos2.size()>0){
            ep = Integer.parseInt(datos2.get(datos2.size()-1).split(" - ")[1].split("</a>")[0]);
        }
        return ep;
    }

    public String getDirectLink(String url_video) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL oracle = new URL(url_video);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        String inputLine;
        ArrayList<String> datos = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            datos.add(inputLine);
        }
        in.close();
        String url = "nada";
        for (int i = 0; i < datos.size(); i++) {
            if (url_video.startsWith("https://jkanime.net/um.php")) {
                if (datos.get(i).contains("swarmId:")) {
                    url = datos.get(i).split("swarmId: '")[1].split("',")[0];
                }
            } else {
                if (url_video.startsWith("https://jkanime.net/jk.php")) {
                    if (datos.get(i).contains("source")) {
                        url = datos.get(i).split("source src=\"")[1].split("\" type")[0];
                    }
                }
            }
        }
        Log.d("URL DIRECTOOOOOO: " , url);
        return url;
    }

}
