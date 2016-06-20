package com.example.domin.weatherapp.RestService;

import com.example.domin.weatherapp.RestService.JsonObjects.WeatherResponse;

import java.util.Date;

import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by Dominik Ratajczak on 18.06.2016.
 */
public interface WebService {

    @GET("/")
    void postWeatherDate(Callback<WeatherResponse> pResponse);
}
