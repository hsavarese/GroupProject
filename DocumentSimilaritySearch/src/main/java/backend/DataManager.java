package backend;

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
                this.collection = database.getCollection("documents");
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
