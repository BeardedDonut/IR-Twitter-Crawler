package com.gilgamesh.crawler;

import com.gilgamesh.crawler.Constants.ProjectConstants;
import com.gilgamesh.crawler.helpers.City;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 6/28/18.
 */
public class TwitterMiddleware {
    private Twitter twitter;
    private PrintWriter consolePrint;
    private List<Status> statusList;


    TwitterMiddleware() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(ProjectConstants.CONSUMER_KEY)
                .setOAuthConsumerSecret(ProjectConstants.CONSUMER_SECRET)
                .setOAuthAccessToken(ProjectConstants.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ProjectConstants.ACCESS_TOKEN_SECRET)
                .setTweetModeExtended(true);

        this.twitter = new TwitterFactory(cb.build()).getInstance();
        this.statusList = new ArrayList<Status>();
    }

    public void queryHandle(String handle) throws IOException, TwitterException {
        
    }
    int counter = 0;

    public void searchQuery() {

        for(City c: ProjectConstants.cities) {
            for(String keyword: ProjectConstants.firstOrderKeyWords) {
                Query query = new Query(keyword);
                query.setCount(100);
                query.setGeoCode(new GeoLocation(c.getLatitude(), c.getLongitude()), 100, Query.KILOMETERS);
                query.setSince("2017-01-01");

                try {
                    QueryResult result = twitter.search(query);
                    System.out.println("Count: " + result.getTweets().size());

                    for (Status tweet : result.getTweets()) {
                        if (tweet.isRetweet()) {
                            tweet = tweet.getRetweetedStatus();
                        }
                        counter++;

                        writeTweet(counter, tweet, c.getName());
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void writeTweet(int tweetNumber, Status tweet, String estimatedPlace) throws IOException {
        FileWriter fw = new FileWriter(ProjectConstants.TWEETS_PATH + tweetNumber + ".txt");
        PrintWriter pr = new PrintWriter(fw);

        String place = "unknown";
        if (tweet.getPlace() != null) {
            place = tweet.getPlace().getFullName();
        }

        pr.write(place + "|" +  tweet.getCreatedAt().toString()+"\n@" + tweet.getUser().getName() + "\n" + tweet.getText());
        pr.close();
    }

    public static void main(String[] args) {
            TwitterMiddleware tw = new TwitterMiddleware();
            tw.searchQuery();
    }
}
