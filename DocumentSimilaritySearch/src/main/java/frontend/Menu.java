package frontend;

import nlp.Newspaper;
import backend.Database;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    public void startUp() {
        // Create a collection in the database to store Newspaper objects
        Database newspaperDatabase = new Database("newspaper_app_database", "newspaper_data");
        newspaperDatabase.createCollection();

        // Parse output.json
        String jsonFile = "C:\\Users\\Sima\\Downloads\\DocumentSimilaritySearch\\src\\main\\resources\\output.json"; // Adjust path accordingly
        String line;
        StringBuilder jsonString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonString.toString());

            // Loop through the states and newspapers
            for (String state : jsonObject.keySet()) {
                JSONArray newspapers = jsonObject.getJSONObject(state).getJSONArray("newspaper");
                for (int i = 0; i < newspapers.length(); i++) {
                    JSONObject newspaperJson = newspapers.getJSONObject(i);

                    String name = newspaperJson.getString("name");
                    String video = newspaperJson.optString("video", "");
                    String twitter = newspaperJson.optString("twitter", "");
                    String website = newspaperJson.optString("website", "");
                    String facebook = newspaperJson.optString("facebook", "");
                    String instagram = newspaperJson.optString("instagram", "");
                    String youtube = newspaperJson.optString("youtube", "");
                    String wikipedia = newspaperJson.optString("wikipedia", "");
                    String cityCountyName = newspaperJson.getString("city-county-name");
                    String usState = newspaperJson.getString("us-state");
                    String extractedFrom = newspaperJson.getString("extracted-from");

                    // Create a new Newspaper object
                    Newspaper newspaper = new Newspaper(name, video, twitter, website, facebook, instagram, youtube, wikipedia, cityCountyName, usState, extractedFrom);

                    // Add the newspaper to the database
                    newspaperDatabase.addToDatabase(newspaper.getDocument());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutDown() {
        Database newspaperDatabase = new Database("newspaper_app_database", "newspaper_data");
        newspaperDatabase.deleteCollection();
    }

    public static void main(String[] args) {
        System.out.println("Initializing the newspaper app...");

        // Call the startUp method
        Menu menu = new Menu();
        menu.startUp();

        // Print menu options
        System.out.println("Hello! Welcome to the newspaper app!");
        System.out.println("1. Add a newspaper to the database.");
        System.out.println("2. Get details of a newspaper from the database.");
        System.out.println("3. Find similar newspapers.");
        System.out.println("4. Exit.");

        System.out.print("Enter your choice: ");
        try (Scanner scanner = new Scanner(System.in)) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Add newspaper logic (similar to movie)
                    break;
                case 2:
                    System.out.println("Get details of a newspaper.");
                    break;
                case 3:
                    System.out.println("Find similar newspapers.");
                    break;
                case 4:
                    System.out.println("Exiting the newspaper app...");
                    menu.shutDown();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
