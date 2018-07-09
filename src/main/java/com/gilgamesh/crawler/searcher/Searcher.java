package com.gilgamesh.crawler.searcher;

import com.gilgamesh.crawler.constants.ProjectConstants;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/7/18.
 */
public class Searcher {
    private IndexSearcher indexSearcher;
    private QueryParser queryParser;

    private static Searcher searcherInstance;

    private Searcher(String indexDirectoryPath, CharArraySet stopWords) throws IOException {
        Directory indexDir = FSDirectory.open(Paths.get(ProjectConstants.INDEX_DIRECTORY));
        IndexReader indexReader = DirectoryReader.open(indexDir);

        indexSearcher = new IndexSearcher(indexReader);

        queryParser = new QueryParser(ProjectConstants.TWEET_CONTENT_NAME, new EnglishAnalyzer(stopWords));
    }

    public TopDocs search(String queryString) throws IOException, ParseException {
        Query query = generateQuery(queryString);
        return indexSearcher.search(query, 10);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public Query generateQuery(String queryString) throws ParseException {
        return queryParser.parse(queryString);
    }

    public static void main(String[] args) {

    }
}
