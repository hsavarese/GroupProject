package backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
// import nlp.FeatureExtractor;
// import nlp.SimilarityCalculator;

import java.util.List;

public class DataManager {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

        public DataManager() {
            try (MongoClient mongoClient = new MongoClient("mongodb+srv://denis:COS225@documentbase.6wttq.mongodb.net/?retryWrites=true&w=majority&appName=DocumentBase")) {
                this.database = mongoClient.getDatabase("");
                this.collection = database.getCollection("");
            }
        }

    // Create operation: Upload a document and store its path and features
    public void createDocument(String path) {
        List<String> document = ExtractDocument.extractDocument(path);
        Document doc = new Document("path", path).append("features", features);
        collection.insertOne(doc);
        System.out.println("Document uploaded to MongoDB successfully!");
    }
}
