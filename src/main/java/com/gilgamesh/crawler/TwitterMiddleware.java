package com.gilgamesh.crawler;

import com.gilgamesh.crawler.Constants.ProjectConstants;
import com.gilgamesh.crawler.helpers.City;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 6/28/18.
 */
public class TwitterMiddleware {
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statusList;


    TwitterMiddleware(PrintStream consolePrint) {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(ProjectConstants.CONSUMER_KEY)
                .setOAuthConsumerSecret(ProjectConstants.CONSUMER_SECRET)
                .setOAuthAccessToken(ProjectConstants.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ProjectConstants.ACCESS_TOKEN_SECRET);

        this.twitter = new TwitterFactory(cb.build()).getInstance();
        this.consolePrint = consolePrint;
        this.statusList = new ArrayList<Status>();
    }

    public void queryHandle(String handle) throws IOException, TwitterException {
        
    }

    public void searchQuery(String searchTerm) {
        for(City c:  ProjectConstants.cities) {
            Query query = new Query(searchTerm);
            query.setCount(100);
            query.setGeoCode(new GeoLocation(c.getLatitude(), c.getLongitude()), 100, Query.KILOMETERS);
            query.setSince("2017-01-01");

            try {
                QueryResult result = twitter.search(query);
                int counter = 0;
                System.out.println("Count: " + result.getTweets().size());

                for (Status tweet : result.getTweets()) {
                    counter++;
                    consolePrint.println("#" + counter + "\n@" + tweet.getUser().getName() + "\n " + tweet.getText());
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        TwitterMiddleware tw = new TwitterMiddleware(System.out);

        tw.searchQuery("restaurant");
    }
}
