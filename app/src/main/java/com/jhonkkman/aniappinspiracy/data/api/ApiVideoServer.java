package com.jhonkkman.aniappinspiracy.data.api;


import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhonkkman.aniappinspiracy.CenterActivity;
import com.jhonkkman.aniappinspiracy.data.models.Extra;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.jhonkkman.aniappinspiracy.CenterActivity.extras;

public class ApiVideoServer {

    private String BASE_URL;
    private String ANIME_NAME;
    private int EPISODE;

    public ApiVideoServer() {

    }

    public ApiVideoServer(String ANIME_NAME, int EPISODE) {
        this.ANIME_NAME = ANIME_NAME;
        this.EPISODE = EPISODE;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public ArrayList<String> getVideoServersSub() throws IOException {
        BASE_URL = "https://jkanime.net/";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (ANIME_NAME.contains("__")) {
            ANIME_NAME = ANIME_NAME.replace("__", "_");
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
        String url = "";
        if (extras.getSub_es() != null) {
            for (int i = 0; i < extras.getSub_es().size(); i++) {
                if (ANIME_NAME.equals(extras.getSub_es().get(i).split(",")[0])) {
                    anime_final = extras.getSub_es().get(i).split(",")[1];
                    break;
                }
            }
        }
        url = BASE_URL + anime_final + "/" + EPISODE;
        URL oracle = new URL(url);
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

    public ArrayList<String> getVideoServersLat() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (ANIME_NAME.contains("__")) {
            ANIME_NAME = ANIME_NAME.replace("__", "_");
        }
        String[] dat = ANIME_NAME.split("_");
        String anime_final = "";
        String url = "";
        boolean stateS = false;
        if (extras.getLatino() != null) {
            for (int i = 0; i < extras.getLatino().size(); i++) {
                Log.d("POSITIONEXTRA","" +i);
                if (extras.getLatino().get(i).contains(",")) {
                    if (ANIME_NAME.equalsIgnoreCase(extras.getLatino().get(i).split(",")[0])) {
                        BASE_URL = "https://henaojara.com/";
                        if (extras.getLatino().get(i).contains(";")) {
                            anime_final = extras.getLatino().get(i).split(",")[1].replace(";", "");
                            url = BASE_URL + "/" + anime_final;
                        } else {
                            anime_final = extras.getLatino().get(i).split(",")[1];
                            url = BASE_URL + "/episode/" + anime_final + "-espanol-latino-hd-1x" + EPISODE;
                        }
                        stateS = true;
                    }
                } else {
                    if (extras.getLatino().get(i).contains("#")) {
                        if (ANIME_NAME.equalsIgnoreCase(extras.getLatino().get(i).split("#")[0])) {
                            if (extras.getLatino().get(i).contains("#")) {
                                BASE_URL = "https://www.animelatinohd.com/ver/";
                                anime_final = extras.getLatino().get(i).split("#")[1];
                                url = BASE_URL + anime_final + "/" + EPISODE;
                                stateS = true;
                            }
                        }
                    }
                }
            }
        }
        if(!stateS){
            BASE_URL = "https://www.animelatinohd.com/ver/";
            for (int i = 0; i < dat.length; i++) {
                if (i == dat.length - 1) {
                    anime_final = anime_final + dat[i].toLowerCase();
                } else {
                    anime_final = anime_final + dat[i].toLowerCase() + "-";
                }
            }
            url = BASE_URL + anime_final + "/" + EPISODE;
        }
        URL oracle = new URL(url);
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
            if (datos.get(i).contains("TPlayerTb Current")) {
                String[] videos = datos.get(i).split("src=");
                for (int j = 1; j < videos.length; j++) {
                    if (j == 1) {
                        url_videos.add(datos.get(i).trim().split("src=\"")[1].split("\" frameborder")[0].replace("#038;", ""));
                    } else {
                        url_videos.add(videos[j].split("quot;")[1].replace("amp;", "").replace("#038;", ""));
                    }
                }
                for (int j = 0; j < url_videos.size(); j++) {
                    Log.d("URLVIDEOSLATINO", url_videos.get(j));
                }
            } else {
                if (datos.get(i).contains("__NEXT_DATA__")) {
                    if(datos.get(i).contains("\"languaje\":\"0\"")){
                        String[] datos2 = datos.get(i).split("\\[\\{\"id\":")[2].split("\"code\":\"");
                        ArrayList<String> datosF = new ArrayList<>();
                        for (int j = 1; j < datos2.length; j++) {
                            datosF.add(datos2[j].split("\",\"languaje\":\"1\",")[0]);
                        }
                        Log.d("urlAProbar", "data :  " + datosF.toString());
                        url_videos = datosF;
                    }else{
                        String[] datos2 = datos.get(i).split("\\[\\{\"id\":")[1].split("\"code\":\"");
                        ArrayList<String> datosF = new ArrayList<>();
                        for (int j = 1; j < datos2.length; j++) {
                            datosF.add(datos2[j].split("\",\"languaje\":\"1\",")[0]);
                        }
                        Log.d("urlAProbar", "data :  " + datosF.toString());
                        url_videos = datosF;
                    }
                    Log.d("urlAProbar","data :  " + datos.get(i));

                    break;
                }
            }
        }
        return url_videos;
    }

    public int getCountEpisodes() throws IOException {
        BASE_URL = "https://jkanime.net/";
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
            if (datos.get(i).contains("<a class=\"numbers\"")) {
                Log.d("NUMBRES", datos.get(i) + "CAN");
                datos2.add(datos.get(i));
            }
        }
        int ep = 0;
        if (datos2.size() > 0) {
            ep = Integer.parseInt(datos2.get(datos2.size() - 1).split(" - ")[1].split("</a>")[0]);
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
        Log.d("URL DIRECTOOOOOO: ", url);
        return url;
    }

}
