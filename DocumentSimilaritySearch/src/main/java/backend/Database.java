package backend;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String connectionString;
    private String databaseName;
    private String collectionName;

    // Constructor for default connection string and custom database/collection names
    public Database(String dbName, String collectionName) {
        this.connectionString = "mongodb+srv://denis:COS225@documentbase.6wttq.mongodb.net/?retryWrites=true&w=majority&appName=DocumentBase";
        this.databaseName = dbName;
        this.collectionName = collectionName;
    }

    // Constructor to specify everything including the connection string
    public Database(String connectionString, String dbName, String collectionName) {
        this.connectionString = connectionString;
        this.databaseName = dbName;
        this.collectionName = collectionName;
    }

    // Connect to the database and return the MongoDatabase object
    public MongoDatabase connectToDatabase() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            return mongoClient.getDatabase(this.databaseName);
        } catch (Exception e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
            return null;
        }
    }

    // Add a document to the database
    public void addToDatabase(Document document) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            MongoCollection<Document> collection = database.getCollection(this.collectionName);
            collection.insertOne(document);
        }
    }

    // Create a collection in the database
    public void createCollection() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            database.createCollection(this.collectionName);
        }
    }

    // Delete the entire collection from the database
    public void deleteCollection() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            database.getCollection(this.collectionName).drop();
        }
    }

    // Delete all documents in the collection
    public void deleteAllDocuments() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            MongoCollection<Document> collection = database.getCollection(this.collectionName);
            collection.deleteMany(new Document());
        }
    }

    // Retrieve all documents from the collection
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            MongoCollection<Document> collection = database.getCollection(this.collectionName);
            collection.find().into(documents);  // Retrieves all documents from the collection
        } catch (Exception e) {
            System.out.println("Error retrieving documents: " + e.getMessage());
        }
        return documents;
    }
}
