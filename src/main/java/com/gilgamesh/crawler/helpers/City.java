package com.gilgamesh.crawler.helpers;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/3/18.
 */
public class City {
    private String name;
    private float latitude;
    private float longitude;

    public City(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
