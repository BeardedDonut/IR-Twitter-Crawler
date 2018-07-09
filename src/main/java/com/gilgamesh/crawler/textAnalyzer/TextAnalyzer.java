package com.gilgamesh.crawler.textAnalyzer;

import com.gilgamesh.crawler.helpers.TextAnalysisResult;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Tweets Text Analyzer class
 * Singleton pattern applies cause creating an analyzer was quiet time consuming so
 * I thought it'd be better to apply singleton.
 *
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/9/18.
 */
public class TextAnalyzer {
    private static TextAnalyzer tweetTextAnalyzer;
    private StanfordCoreNLP pipeline;

    /**
     * Constructor
     */
    private TextAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }


    /**
     * this static method returns an instance of
     *
     * @return {TextAnalyzer} retunrs textAnalyzer instance
     */
    public static TextAnalyzer getInstance() {
        if (tweetTextAnalyzer == null) {
            tweetTextAnalyzer = new TextAnalyzer();
        }

        return tweetTextAnalyzer;
    }


    /**
     * Text Analysis over tweets text
     * <p>
     * Heuristic for finding restaurant's name: if it is a LOCATION and the word is a Noun then it is probably a restaurant
     * Heuristic for finding restaurant's City: if it is a CITY and the word is a Noun then it is probably a restaurant
     * @param tweet: tweet's text
     * @return
     */
    public TextAnalysisResult analyzeTweet(String tweet) {
        // annotate the document
        Annotation doc = new Annotation(tweet);
        pipeline.annotate(doc);

        TextAnalysisResult analysisResult = new TextAnalysisResult();
        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);

        // check each sentence analysis result
        for (CoreMap sentence : sentences) {
            StringBuilder restaurantName = new StringBuilder();
            StringBuilder cityName = new StringBuilder();

            // check each word annotation result
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                System.out.println(String.format("word: [%s] pos: [%s] ne: [%s]", word, pos, ne));

                // applying the said above heuristic
                if (ne.equals("LOCATION") && pos.equals("NNP")) {
                    restaurantName.append(" ").append(word);        // appending the name
                } else if (!restaurantName.toString().equals("")) {
                    analysisResult.setRestaurantName(restaurantName.toString());
                    restaurantName = new StringBuilder();
                }

                // applying the said above heuristic for finding the cities
                if (ne.equals("CITY") && pos.equals("NNP")) {
                    cityName.append(" ").append(word);
                } else if (!cityName.toString().equals("")) {
                    analysisResult.setCity(cityName.toString());
                    cityName = new StringBuilder();
                }
            }

            if (!restaurantName.toString().equals("")) {
                analysisResult.setRestaurantName(restaurantName.toString());
            }

            if (!cityName.toString().equals("")) {
                analysisResult.setCity(cityName.toString());
            }
        }

        // setting the default rating
        analysisResult.setRating("Neutral");

        // rate the restaurant based on sentiment analysis of the tweet's text
        if (analysisResult.getRestaurantName() != null) {
            String sentiment = analyzeSentiment(doc);
            analysisResult.setRating(sentiment);
        }

        return analysisResult;
    }


    /**
     * this method uses sentiment analysis over tweet's text for specifying each restaurant's rating
     * @param doc: document to apply sentiment analysis
     * @return {String} indication of the rating [Very Positive, Positive, Neutral, Negative, Very Negative]
     */
    private String analyzeSentiment(Annotation doc) {
        pipeline.annotate(doc);

        HashMap<String, Integer> sentimentCount = this.getRawSentimentHashMap();

        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            if (sentimentCount.get(sentiment) != null) {
                sentimentCount.put(sentiment, sentimentCount.get(sentiment) + 1);
            }
        }

        return findMaxOccurredSentiment(sentimentCount);
    }


    /**
     * a helper method to help finding an overall rating for a tweet's text
     * it uses maximum occurred sense.
     * @param sentimentMap: sentiment map of each tweet
     * @return {String} most occurred sense.
     */
    private String findMaxOccurredSentiment(HashMap<String, Integer> sentimentMap) {
        int max = -1;
        String selectedSentiment = "";

        for (String sentiment : sentimentMap.keySet()) {
            if (sentimentMap.get(sentiment) > max) {
                selectedSentiment = sentiment;
                max = sentimentMap.get(sentiment);
            }
        }

        return selectedSentiment;
    }

    private HashMap<String, Integer> getRawSentimentHashMap() {
        HashMap<String, Integer> sentimentCount = new HashMap<>();

        sentimentCount.put("Neutral", 0);
        sentimentCount.put("Very Negative", 0);
        sentimentCount.put("Negative", 0);
        sentimentCount.put("Positive", 0);
        sentimentCount.put("Very Positive", 0);

        return sentimentCount;
    }
}
