package frontend;

import backend.Database;
import org.bson.Document;
import nlp.Newspaper;
import nlp.SimilarityCalculator;
import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;


public class Menu {

    public static void main(String[] args) {
        // Initialize the Database with your database name and collection name
        Database database = new Database("newspaper_app_database2", "newspaper_data2");

        // Use the connectToDatabase method to get a connection
        MongoDatabase mongoDatabase = database.connectToDatabase();

        if (mongoDatabase == null) {
            System.out.println("Failed to connect to the database. Exiting...");
            return;
        }

        // Print database name to confirm successful connection
        System.out.println("Connected to database: " + mongoDatabase.getName());

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Load up to 100 newspapers before presenting the menu
        startUp(database);

        // Print welcome message and show menu options
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Welcome to the newspaper app!");
        System.out.println(" ");

        while (true) {
            // Display the menu
            System.out.println("__________________________________________________________");
            System.out.println("1. Add a newspaper to the database.");
            System.out.println(" ");
            System.out.println("2. Get details of a newspaper from the database.");
            System.out.println(" ");
            System.out.println("3. Find similar newspapers.");
            System.out.println(" ");
            System.out.println("4. Exit.");
            System.out.println(" ");
            System.out.println("__________________________________________________________");

            // Get user choice
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character after int input

            switch (choice) {
                case 1:
                    // Add a newspaper to the database
                    addNewspaperToDatabase(scanner, database);
                    break;
                case 2:
                    // Get details of a newspaper from the database
                    getNewspaperDetails(scanner, database);
                    break;
                case 3:
                    // Find similar newspapers
                    findSimilarNewspapers(scanner, database);
                    break;
                case 4:
                    // Exit the program
                    System.out.println(" ");
                    System.out.println("Exiting the application...");
                    shutDown(database);
                    scanner.close();
                    return;
                default:
                    System.out.println(" ");
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to add 100 newspapers to the database
    private static void startUp(Database database) {
        // Path to the JSON file
        String jsonFile = "C:\\Users\\Sima\\Downloads\\DocumentSimilaritySearch\\src\\main\\resources\\output.json"; //if running through WSL use a the correct path format
        StringBuilder jsonString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonString.toString());

            // Counter for the number of newspapers added
            int count = 0;

            // Loop through the states and newspapers in the JSON
            for (String state : jsonObject.keySet()) {
                JSONObject stateJson = jsonObject.getJSONObject(state);
                if (stateJson.has("newspaper")) {
                    JSONArray newspapers = stateJson.getJSONArray("newspaper");
                    for (int i = 0; i < newspapers.length(); i++) {
                        if (count >= 10) {
                            // Stop adding more documents once 100 newspapers have been added
                            System.out.println("100 newspapers have been added to the database.");
                            return;  // Exit the method to stop adding more newspapers
                        }

                        JSONObject newspaperJson = newspapers.getJSONObject(i);

                        // Safely extract fields from JSON
                        String name = newspaperJson.optString("name", "");
                        String video = newspaperJson.optString("video", "");
                        String twitter = newspaperJson.optString("twitter", "");
                        String website = newspaperJson.optString("website", "");
                        String facebook = newspaperJson.optString("facebook", "");
                        String instagram = newspaperJson.optString("instagram", "");
                        String youtube = newspaperJson.optString("youtube", "");
                        String wikipedia = newspaperJson.optString("wikipedia", "");
                        String cityCountyName = newspaperJson.optString("city-county-name", "");
                        String usState = newspaperJson.optString("us-state", "");
                        String extractedFrom = newspaperJson.optString("extracted-from", "");

                        // Create a new Newspaper object
                        Newspaper newspaper = new Newspaper(name, video, twitter, website, facebook, instagram, youtube, wikipedia, cityCountyName, usState, extractedFrom);

                        // Add the newspaper to the database
                        database.addToDatabase(newspaper.getDocument());

                        // Increment the counter after adding a newspaper
                        count++;
                    }
                }
            }

            System.out.println(" ");
            System.out.println("Newspapers data successfully added to the database!");

        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while processing the data: " + e.getMessage());
        }
    }

    // Method to add a newspaper to the database
    private static void addNewspaperToDatabase(Scanner scanner, Database database) {
            System.out.println(" ");
            System.out.println("Enter the name of the newspaper: ");
            String name = scanner.nextLine().trim();

           
            System.out.println("Enter the video URL (default: empty): ");
            String video = scanner.nextLine().trim();

      
            System.out.println("Enter the Twitter handle (default: empty): ");
            String twitter = scanner.nextLine().trim();

         
            System.out.println("Enter the website URL (default: empty): ");
            String website = scanner.nextLine().trim();

           
            System.out.println("Enter the Facebook URL (default: empty): ");
            String facebook = scanner.nextLine().trim();
            
           
            System.out.println("Enter the Instagram URL (default: empty): ");
            String instagram = scanner.nextLine().trim();
    
            System.out.println("Enter the YouTube URL (default: empty): ");
            String youtube = scanner.nextLine().trim();
    
            System.out.println("Enter the Wikipedia URL (default: empty): ");
            String wikipedia = scanner.nextLine().trim();
    
            System.out.println("Enter the city/county name: ");
            String cityCountyName = scanner.nextLine().trim();
    
            System.out.println("Enter the U.S. state: ");
            String usState = scanner.nextLine().trim();
    
            System.out.println("Enter the source of extraction: ");
            String extractedFrom = scanner.nextLine().trim();
    
            Newspaper newspaper = new Newspaper(name, video, twitter, website, facebook, instagram,
                    youtube, wikipedia, cityCountyName, usState, extractedFrom);
    
            Document newspaperDocument = newspaper.getDocument();
            database.addToDatabase(newspaperDocument);

            System.out.println(" ");
            System.out.println("Newspaper added successfully!");
    
    }

    // Method to get details of a newspaper from the database
    private static void getNewspaperDetails(Scanner scanner, Database database) {
        System.out.println(" ");
        System.out.print("Enter the name of the newspaper to fetch details: ");
        String name = scanner.nextLine().trim();

        try {
            List<Document> documents = database.getAllDocuments();
            boolean found = false;
            for (Document doc : documents) {
                if (doc.getString("name").equalsIgnoreCase(name)) {
                    found = true;
                    System.out.println("Newspaper details: ");
                    System.out.println(doc.toJson());
                    break;
                }
            }
            if (!found) {
                System.out.println("No newspaper found with the name: " + name);
            }
        } catch (Exception e) {
            System.out.println("Error fetching details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to find similar newspapers
    private static void findSimilarNewspapers(Scanner scanner, Database database) {
        System.out.println(" ");
        System.out.println("Finding similar newspapers...");
    
        // Retrieve all documents from the database
        List<Document> documents = null;
        try {
            documents = database.getAllDocuments();
        } catch (Exception e) {
            System.out.println("Error fetching documents from database: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    
        // Initialize the SimilarityCalculator with the stopwords file
        SimilarityCalculator similarityCalculator = new SimilarityCalculator();
    
        // Add documents from the database to the SimilarityCalculator
        for (Document doc : documents) {
            String name = doc.getString("name");
            String content = doc.getString("name") + " " + doc.getString("website"); // You can use the content of the document here, adjust as needed
            similarityCalculator.addArticle(name, content);
        }
    
        // Calculate the IDF values after all documents are added
        similarityCalculator.calculateIDF();
    
        // Ask user for input to find similar newspapers
        
        System.out.print("Enter a newspaper or topic to find similar newspapers: ");
        String inputText = scanner.nextLine();
    
        // Get the recommended similar newspapers
        List<String> similarNewspapers = similarityCalculator.recommendArticles(inputText, 5);  // Get top 5 recommendations
    
        if (similarNewspapers.isEmpty()) {
            System.out.println("No similar newspapers found.");
        } else {
            System.out.println("Similar Newspapers: ");
            for (String newspaper : similarNewspapers) {
                System.out.println(newspaper);
            }
        }
    }

    //Deletes the collection when the user exits. 
    public static void shutDown(Database database) {
        database.deleteCollection();  
        System.out.println("Collection deleted successfully.");
    }
}
