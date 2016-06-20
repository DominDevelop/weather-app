package com.example.domin.weatherapp.RestService;

/**
 * Created by Dominik Ratajczak on 18.06.2016.
 */
public class RestUri {

    private static final String URI = "https://api.forecast.io/forecast/54c99370a3e57f4d72c37961a8ecd435/";

    public static String buildUri(double lon, double lat, String time){

        String uri = URI + lon + "," + lat + "," + time;
        return uri;
    }
}
