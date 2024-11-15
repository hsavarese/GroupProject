package backend;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ArticleManager {
    private MongoCollection<Document> articleCollection;
    private static final String CSV_FILE_PATH = "articles_log.csv";

    // takes article as perameter
    public ArticleManager(MongoCollection<Document> articleCollection) {
        this.articleCollection = articleCollection;
        initializeCsvFile();
    }

    // initializes csv file if it doesn't exist
    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, false)) {
            writer.append("Name,Website,Media Class,Media Subclass,State,City,Social Links,Extracted From\n");
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }

    // adds an Article object to the MongoDB collection and logs it to the CSV file
    public void addArticle(Article article) {
        if (article == null) {
            System.err.println("Invalid article: Need more information");
            return;
        }
        try {
            // Add to MongoDB
            Document articleDoc = article.getDocument();
            articleCollection.insertOne(articleDoc);
            System.out.println("Article added to MongoDB: " + article.getName());

            // Log to CSV
            logArticleToCsv(article);
        } catch (Exception e) {
            System.err.println("Failed to add article: " + e.getMessage());
        }
    }

    // Logs an article to the CSV file
    private void logArticleToCsv(Article article) {
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
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    //add article with raw values
    public void addArticle(
            String name, String website, String mediaClass, String mediaSubclass,
            String usState, String cityCountyName, Map<String, String> socialLinks, String extractedFrom) {
        try {
            // Create an Article object
            Article article = new Article(name, website, mediaClass, mediaSubclass, usState, cityCountyName, socialLinks, extractedFrom);

            // Add the article
            addArticle(article);
        } catch (Exception e) {
            System.err.println("Failed to add article: " + e.getMessage());
        }
    }

    // Lists all articles 
    public void listArticles() {
        System.out.println("Listing all articles in the database:");
        for (Document doc : articleCollection.find()) {
            System.out.println(doc.toJson());
        }
    }
}

