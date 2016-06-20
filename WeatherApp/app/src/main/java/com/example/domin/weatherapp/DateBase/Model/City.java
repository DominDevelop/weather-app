package com.example.domin.weatherapp.DateBase.Model;

/**
 * Created by Dominik Ratajczak on 18.06.2016.
 *
 */
public class City {

    private int id;
    private String name;
    private double lat;
    private double lon;
    private double averageC;
    private double averageF;
    private String day;

    public City(){};

    public City(int id, String name, double lat, double lon, double averageC, double averageF, String day) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.averageC = averageC;
        this.averageF = averageF;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAverageC() {
        return averageC;
    }

    public void setAverageC(double averageC) {
        this.averageC = averageC;
    }

    public double getAverageF() {
        return averageF;
    }

    public void setAverageF(double averageF) {
        this.averageF = averageF;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

