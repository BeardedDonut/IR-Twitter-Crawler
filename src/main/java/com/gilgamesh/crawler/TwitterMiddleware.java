package com.gilgamesh.crawler;

import com.gilgamesh.crawler.constants.ProjectConstants;
import com.gilgamesh.crawler.helpers.City;
import com.gilgamesh.crawler.helpers.TextAnalysisResult;
import com.gilgamesh.crawler.textAnalyzer.TextAnalyzer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;

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

    private static PrintWriter textAnalysisWriter;


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

    public void searchQuery() throws IOException {

        for(City c: ProjectConstants.cities) {
            for(String keyword: ProjectConstants.firstOrderKeyWords) {
                Query query = new Query(keyword);
                query.setCount(100);
                query.setGeoCode(new GeoLocation(c.getLatitude(), c.getLongitude()), 100, Query.KILOMETERS);
                query.setSince("2016-01-01");

                FileWriter fw = new FileWriter(ProjectConstants.FINAL_OUTPUT_LIST_PATH);

                textAnalysisWriter = new PrintWriter(fw);

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

    /**
     *
     * @param tweetNumber
     * @param tweet
     * @param estimatedPlace
     * @throws IOException
     */
    public static void writeTweet(int tweetNumber, Status tweet, String estimatedPlace) throws IOException {
        FileWriter fw = new FileWriter(ProjectConstants.TWEETS_PATH + tweetNumber + ".txt");
        PrintWriter pr = new PrintWriter(fw);

        String place = estimatedPlace;
        if (tweet.getPlace() != null) {
            place = tweet.getPlace().getFullName();
        }

        pr.write(place + "|" +  tweet.getCreatedAt().toString()+"\n@" + tweet.getUser().getName() + "\n" + tweet.getText());
        pr.close();

        writeTweetAnalysis(tweetNumber, tweet, estimatedPlace);
    }

    /**
     * This method writes each tweet's text analysis result in a file
     * @param tweetNumber
     * @param tweet
     * @param estimatedCity
     * @throws IOException
     */
    public static void writeTweetAnalysis(int tweetNumber, Status tweet, String estimatedCity) throws IOException {
        TextAnalyzer ta = TextAnalyzer.getInstance();
        TextAnalysisResult tar = ta.analyzeTweet(tweet.getText());
        tar.setTweetId(tweetNumber);

        if(tar.getCity() == null) {
            tar.setCity(estimatedCity);
        }

        if(tar.getRestaurantName() != null) {
            textAnalysisWriter.append(tar.toString());
        }
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public PrintWriter getConsolePrint() {
        return consolePrint;
    }

    public void setConsolePrint(PrintWriter consolePrint) {
        this.consolePrint = consolePrint;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    public static PrintWriter getTextAnalysisWriter() {
        return textAnalysisWriter;
    }

    public static void setTextAnalysisWriter(PrintWriter textAnalysisWriter) {
        TwitterMiddleware.textAnalysisWriter = textAnalysisWriter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
