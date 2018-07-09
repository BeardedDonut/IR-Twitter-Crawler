package com.gilgamesh.crawler.helpers;

/**
 * This class contains essential information for a Tweet
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/6/18.
 */
public class Tweet {
    private String text;
    private String name;
    private String date;
    private String place;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
