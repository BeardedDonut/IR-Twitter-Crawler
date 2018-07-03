package com.gilgamesh.crawler.Constants;

import com.gilgamesh.crawler.helpers.City;

import java.util.ArrayList;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/3/18.
 */
public class ProjectConstants {
    public static final String CONSUMER_KEY="GmZ9rlWqvA9y98ZlUovTJ13L6";
    public static final String CONSUMER_SECRET="ZILtVQmoYj3JxKQ94p33nLDG5lMXJYjGnDIgGyESgdrnaa6teE";
    public static final String ACCESS_TOKEN="705920283048173569-OVlqP6YZ18HOy79R3vDi9o54LuYDdle";
    public static final String ACCESS_TOKEN_SECRET="v34aAU5CDcrccSd3rHahFtHh4n4IV7Mbrgc10B2AwtoG9";

    public static ArrayList<City> cities = new ArrayList<City>();
    static {
        cities.add(new City("chicago", 41.8781F, -87.6298F));
        cities.add(new City("new-york", 40.7128F, -74.0060F));
        cities.add(new City("boston",42.3601F, -71.0589F));
        cities.add(new City("seattle", 47.6062F, -122.3321F));
        cities.add(new City("san-francisco", 37.7749F, -122.4194F));
        cities.add(new City("los-angeles", 34.0522F, -118.2437F));
    }

}
