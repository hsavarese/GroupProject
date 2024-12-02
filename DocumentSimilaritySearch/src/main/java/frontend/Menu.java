package frontend;

import backend.Database;
import backend.Article;
import nlp.SimilarityCalculator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private SimilarityCalculator similarityCalculator;

    public void startUp() {
        // Create a database instance to handle articles
        Database articleDatabase = new Database("newspaper_app_database", "newspaper_data");
        articleDatabase.createCollection();

        // Initialize the SimilarityCalculator with stop words file
        similarityCalculator = new SimilarityCalculator("listOfStopWords.txt");

        // Load articles from output.json into the database and the SimilarityCalculator
        String jsonFile = "C:\\Users\\Sima\\Downloads\\DocumentSimilaritySearch\\src\\main\\resources\\output.json"; // Adjust path if needed
        StringBuilder jsonString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonString.toString());

            // Go through all states and their newspapers
            for (String state : jsonObject.keySet()) {
                JSONArray newspapers = jsonObject.getJSONObject(state).getJSONArray("newspaper");
                for (int i = 0; i < newspapers.length(); i++) {
                    JSONObject newspaperJson = newspapers.getJSONObject(i);

                    String name = newspaperJson.getString("name");
                    String website = newspaperJson.optString("website", "");
                    String mediaClass = newspaperJson.optString("media-class", "");
                    String mediaSubclass = newspaperJson.optString("media-subclass", "");
                    String usState = newspaperJson.optString("us-state", "");
                    String cityCountyName = newspaperJson.optString("city-county-name", "");
                    String extractedFrom = newspaperJson.optString("extracted-from", "");

                    // Create an Article object
                    Article article = new Article(name, website, mediaClass, mediaSubclass, usState, cityCountyName, null, extractedFrom);

                    // Add the article to the database
                    articleDatabase.addToDatabase(article.getDocument());

                    // Add the article to the SimilarityCalculator
                    String articleContent = website + " " + mediaClass + " " + cityCountyName + " " + extractedFrom;
                    similarityCalculator.addArticle(name, articleContent);
                }
            }

            // Calculate IDF values for similarity scoring
            similarityCalculator.calculateIDF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        // Clean up the database by deleting the collection
        Database articleDatabase = new Database("newspaper_app_database", "newspaper_data");
        articleDatabase.deleteCollection();
    }

    public static void main(String[] args) {
        Menu menu = new Menu();

        // Start the application
        System.out.println("Starting the newspaper app...");
        menu.startUp();

        // Interactive menu for the user
        System.out.println("Welcome to the newspaper app!");
        System.out.println("1. Add a newspaper to the database.");
        System.out.println("2. Get details of a newspaper from the database.");
        System.out.println("3. Find similar newspapers.");
        System.out.println("4. Exit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Add newspaper logic not implemented yet.");
                        break;
                    case 2:
                        System.out.println("Get details of a newspaper logic not implemented yet.");
                        break;
                    case 3:
                        // Find and recommend similar newspapers
                        System.out.println("Enter text to find similar newspapers:");
                        String inputText = scanner.nextLine();

                        List<String> recommendations = menu.similarityCalculator.recommendArticles(inputText, 5);

                        System.out.println("Recommended Newspapers:");
                        for (String articleId : recommendations) {
                            System.out.println("- " + articleId);
                        }
                        break;
                    case 4:
                        // Exit app
                        System.out.println("Shutting down the app...");
                        menu.shutDown();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }
}
