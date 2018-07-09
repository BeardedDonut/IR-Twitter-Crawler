package com.gilgamesh.crawler.helpers;

/**
 * This class contains each tweet text analysis result
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/9/18.
 */
public class TextAnalysisResult {
    private int tweetId;
    private String rating;
    private String restaurantName;
    private String city;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String getRatingInStars() {
        switch (rating) {
            case "Positive": return "****";
            case "Very Positive": return "*****";
            case "Neutral": return "***";
            case "Negative": return "**";
            case "Very Negative": return "*";
        }

        return "***";
    }

    public String toString() {
        StringBuilder x = new StringBuilder();

        x.append("{\n\t\"name\": ")
                .append("\"" + restaurantName + "\"")
                .append(",\n\t\"city\": ")
                .append("\"" + city + "\"")
                .append(",\n\t\"rating\": ")
                .append("\"" + this.getRatingInStars() + "\"")
                .append(",\n\t\"tweet-id\": ")
                .append("\"" + tweetId + "\"")
                .append("\n},\n");

        return x.toString();
    }
}
