package com.gilgamesh.crawler;


import twitter4j.Twitter;

import java.io.IOException;

public class App {
    public static void main( String[] args ) {
        TwitterMiddleware tw = new TwitterMiddleware();

        try {
            tw.searchQuery();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            TwitterMiddleware.getTextAnalysisWriter().close();
        }
    }
}
