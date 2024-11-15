package backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import nlp.ExtractDocument;

import java.util.Map;

public class DataManager {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public DataManager() {
        try (MongoClient mongoClient = new MongoClient("mongodb+srv://denis:COS225@documentbase.6wttq.mongodb.net/?retryWrites=true&w=majority&appName=DocumentBase")) {
            this.database = mongoClient.getDatabase("DocumentDB");
            this.collection = database.getCollection("documents"); // MongoDB will label it as "documents", but it's used for articles here.
        }
    }

    // Upload a document's metadata to MongoDB
    public void uploadWordFrequencies(Map<String, Integer> frequencyMap, String path) {
        Document doc = new Document("path", path)
                .append("wordFrequencies", frequencyMap);

        collection.insertOne(doc);
        System.out.println("Word frequencies uploaded to MongoDB successfully!");
    }

    // Adds Article object to MongoDB
    public void addArticle(Article article) {
        if (article == null) {
            System.err.println("Invalid article: cannot be null.");
            return;
        }
        try {
            Document articleDoc = article.getDocument();
            collection.insertOne(articleDoc);
            System.out.println("Article added to MongoDB: " + article.getName());
        } catch (Exception e) {
            System.err.println("Failed to add article: " + e.getMessage());
        }
    }

    // Adsd article using raw values
    public void addArticle(
            String name, String website, String mediaClass, String mediaSubclass,
            String usState, String cityCountyName, Map<String, String> socialLinks, String extractedFrom) {
        try {
            Document articleDoc = new Document("name", name)
                    .append("website", website)
                    .append("media-class", mediaClass)
                    .append("media-subclass", mediaSubclass)
                    .append("us-state", usState)
                    .append("city-county-name", cityCountyName)
                    .append("social-links", socialLinks)
                    .append("extracted-from", extractedFrom);

            collection.insertOne(articleDoc);
            System.out.println("Article added to MongoDB: " + name);
        } catch (Exception e) {
            System.err.println("Failed to add article: " + e.getMessage());
        }
    }

    // List all articles in the mongodb
    public void listArticles() {
        System.out.println("Listing all articles in the database:");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

    public static void main(String[] args) {
        DataManager manager = new DataManager();

        // Process document and upload word frequencies to MongoDB
        String inputFile = "C:\\Users\\Sima\\OneDrive\\Documents\\DocumentSimilaritySearch\\src\\main\\java\\backend\\doc.txt";
        ExtractDocument.writeWordFrequency(inputFile);

        // Example: Adding an Article object
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

        // Example: Adding another article using raw values
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

        // List all articles
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

