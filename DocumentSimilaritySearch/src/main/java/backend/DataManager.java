package backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import nlp.ExtractDocument;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DataManager {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private static final String CSV_FILE_PATH = "articles_log.csv";

    public DataManager() {
        // Connect to the database and set up the collection
        try (MongoClient mongoClient = new MongoClient("mongodb+srv://denis:COS225@documentbase.6wttq.mongodb.net/?retryWrites=true&w=majority&appName=DocumentBase")) {
            this.database = mongoClient.getDatabase("DocumentDB");
            this.collection = database.getCollection("documents");
            setupCsvFile(); // Make sure the CSV file is ready to use
        }
    }

    // Set up the CSV file with headers if it doesn't exist
    private void setupCsvFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, false)) {
            writer.append("Name,Website,Media Class,Media Subclass,State,City,Social Links,Extracted From\n");
        } catch (IOException e) {
            System.err.println("Error setting up CSV file: " + e.getMessage());
        }
    }

    // Save word frequencies from a document to the database
    public void uploadWordFrequencies(Map<String, Integer> frequencyMap, String path) {
        Document doc = new Document("path", path)
                .append("wordFrequencies", frequencyMap);

        collection.insertOne(doc);
        System.out.println("Word frequencies saved to MongoDB!");
    }

    // Add an article to MongoDB and log it in the CSV file
    public void addArticle(Article article) {
        if (article == null) {
            System.err.println("Article is missing information, can't save.");
            return;
        }
        try {
            // Save the article in MongoDB
            Document articleDoc = article.getDocument();
            collection.insertOne(articleDoc);
            System.out.println("Saved article to MongoDB: " + article.getName());

            // Add the article info to the CSV
            saveArticleToCsv(article);
        } catch (Exception e) {
            System.err.println("Couldn't save article: " + e.getMessage());
        }
    }

    // Add an article directly with its details (without using the Article object)
    public void addArticle(
            String name, String website, String mediaClass, String mediaSubclass,
            String usState, String cityCountyName, Map<String, String> socialLinks, String extractedFrom) {
        try {
            // Make an Article object with the given info
            Article article = new Article(name, website, mediaClass, mediaSubclass, usState, cityCountyName, socialLinks, extractedFrom);

            // Save the article
            addArticle(article);
        } catch (Exception e) {
            System.err.println("Couldn't save article: " + e.getMessage());
        }
    }

    // Save the article info in the CSV file
    private void saveArticleToCsv(Article article) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.append(article.getName()).append(",")
                  .append(article.getWebsite()).append(",")
                  .append(article.getMediaClass()).append(",")
                  .append(article.getMediaSubclass()).append(",")
                  .append(article.getUsState()).append(",")
                  .append(article.getCityCountyName()).append(",")
                  .append(article.getSocialLinks().toString().replace(",", ";")).append(",")
                  .append(article.getExtractedFrom()).append("\n");
        } catch (IOException e) {
            System.err.println("Error saving article to CSV: " + e.getMessage());
        }
    }

    // Show all articles saved in MongoDB
    public void listArticles() {
        System.out.println("All saved articles:");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

    public static void main(String[] args) {
        DataManager manager = new DataManager();

        // Example: Save word frequencies from a document
        String inputFile = "C:\\Users\\Sima\\OneDrive\\Documents\\DocumentSimilaritySearch\\src\\main\\java\\backend\\doc.txt";
        ExtractDocument.writeWordFrequency(inputFile);

        // Example: Save an article using an Article object
        Map<String, String> socialLinks = Map.of(
                "twitter", "http://www.twitter.com/adndotcom",
                "facebook", "https://www.facebook.com/akdispatch",
                "instagram", "https://www.instagram.com/alaskadispatch/"
        );
        Article article = new Article(
                "Alaska Dispatch News",
                "http://www.adn.com/",
                "newspaper",
                "city-county",
                "Alaska",
                "Anchorage Municipality",
                socialLinks,
                "usnpl.com, thepaperboy.com"
        );
        manager.addArticle(article);

        // Example: Save an article by giving the details directly
        manager.addArticle(
                "Example News",
                "http://www.example.com/",
                "blog",
                "tech",
                "California",
                "San Francisco",
                Map.of("twitter", "http://twitter.com/example"),
                "example.com"
        );

        // Show all articles saved in MongoDB
        manager.listArticles();
    }
}



/*package backend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import nlp.ExtractDocument; 
// import nlp.SimilarityCalculator;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.List;

public class DataManager {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

        public DataManager() {
            try (MongoClient mongoClient = new MongoClient("mongodb+srv://denis:COS225@documentbase.6wttq.mongodb.net/?retryWrites=true&w=majority&appName=DocumentBase")) {
                this.database = mongoClient.getDatabase("DocumentDB");
                this.collection = database.getCollection("documents"); //mongodb will label it has documents, but its articles in here
            }
        }

   // Upload a document's metadata to MongoDB
    public void uploadWordFrequencies(Map<String, Integer> frequencyMap, String path) {
        Document doc = new Document("path", path)
                .append("wordFrequencies", frequencyMap);
    
        collection.insertOne(doc);
        System.out.println("Word frequencies uploaded to MongoDB successfully!");
    }


    public static void main(String[] args) {
        // Define the path to the document file
        String inputFile = "C:\\Users\\Sima\\OneDrive\\Documents\\DocumentSimilaritySearch\\src\\main\\java\\backend\\doc.txt";
        
        // Call the method to process the document and upload word frequencies to MongoDB
        ExtractDocument.writeWordFrequency(inputFile);
    }
}
*/

