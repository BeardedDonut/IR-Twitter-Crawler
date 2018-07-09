package com.gilgamesh.crawler.indexer;

import com.gilgamesh.crawler.constants.ProjectConstants;
import com.gilgamesh.crawler.helpers.TextFilter;
import com.gilgamesh.crawler.helpers.Tweet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/6/18.
 */
public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        File f = new File(indexDirectoryPath);
        Directory indexDirectory = FSDirectory.open(f.toPath());

        IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer());
        writer = new IndexWriter(indexDirectory, config);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    public void indexTweet(File tweetFile, String tweetname) throws IOException {
        Document tweetDoc = analyzeDocument(tweetFile, tweetname);
        writer.addDocument(tweetDoc);
    }

    public void indexAllTweets(String tweetsPath, FileFilter filter) throws IOException {
        // get all the tweets in the tweets path
        File[] files = new File(tweetsPath).listFiles();

        for(File file: files) {

            // check the conditions
            boolean condition = !file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && filter.accept(file);

            if (condition) {
                String name = file.getName();
                indexTweet(file, name);
            }
        }
    }

    private Document analyzeDocument(File file, String filename) throws IOException {
        Document doc = new Document();

        Tweet tw = getTweet(file);

        // content field type
        FieldType contentType = new FieldType();
        contentType.setStoreTermVectors(true);
        contentType.setTokenized(true);
        contentType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);

        Field tweetText = new Field(ProjectConstants.TWEET_CONTENT_NAME, tw.getText(), contentType);

        // author name
        Field authorName = new StringField(ProjectConstants.TWEET_AUTHOR_FIELD, tw.getName(), Field.Store.YES);

        // date field;
        Field date = new StringField(ProjectConstants.TWEET_DATE_FIELD, tw.getDate(), Field.Store.YES);

        // place field
        Field place = new StringField(ProjectConstants.TWEET_PLACE_FIELD, tw.getPlace(), Field.Store.YES);

        // adding fields
        doc.add(tweetText);
        doc.add(authorName);
        doc.add(date);
        doc.add(place);

        // return the doc
        return doc;
    }

    private Tweet getTweet(File file) throws IOException {
        StringBuilder x = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        String place = line.split("|")[0];
        String date = line.split("|")[1];
        String name = br.readLine();

        line = br.readLine();

        while(line != null) {
            x.append(line);
            line = br.readLine();
        }

        Tweet tw = new Tweet();

        tw.setDate(date);
        tw.setName(name);
        tw.setPlace(place);
        tw.setText(x.toString());

        return tw;
    }

    public static void main(String[] args) {
        try {
            Indexer idx = new Indexer("./indexes/");
            idx.indexAllTweets("./tweets/", new TextFilter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
