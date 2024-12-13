package nlp;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SimilarityCalculator {

    private HashSet<String> vocabulary = new HashSet<>();
    private HashMap<String, Float> idf = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> tf = new HashMap<>();
    private Set<String> stopWords = new HashSet<>();

    // Loads the stop words
    public SimilarityCalculator() {
        loadStopWords();
    }

    // Reads through the stopwords
    public void loadStopWords() {
        try (InputStream inputStream = getClass().getResourceAsStream("/listOfStopWords.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
    
            if (inputStream == null) {
                System.out.println("Could not find /listOfStopWords.txt in classpath.");
                return;
            }
    
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading stop words file.");
            e.printStackTrace();
        }
    }

    // Processes input text: lowercases, removes punctuation, and filters stop words
    public String[] processText(String text) {
        text = text.toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
        String[] words = text.split("\\s+");
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (!stopWords.contains(word) && !word.isEmpty()) {
                filteredWords.add(word);
            }
        }
        return filteredWords.toArray(new String[0]);
    }

    public void addArticle(String id, String text) {
        String[] words = processText(text);
        HashMap<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            vocabulary.add(word);
        }
        tf.put(id, wordCount);
    }

    // Calculates the IDF 
    public void calculateIDF() {
        for (String word : vocabulary) {
            int count = 0;
            for (HashMap<String, Integer> wordCount : tf.values()) {
                if (wordCount.containsKey(word)) {
                    count++;
                }
            }
            idf.put(word, (float) Math.log((float) (tf.size() + 1) / (count + 1)));  // Prevent division by zero
        }
    }

    // Gets the TFIDF score 
    public float calculateTFIDF(String id, String word) {
        if (!tf.containsKey(id) || !tf.get(id).containsKey(word)) {
            return 0;
        }
        return tf.get(id).get(word) * idf.getOrDefault(word, 0f);
    }

    // Calculates similarity between article and input text
    public float calculateSimilarity(String id, String[] words) {
        float score = 0;
        for (String word : words) {
            score += calculateTFIDF(id, word);
        }
        return score;
    }

    // Recommending the articles
    public List<String> recommendArticles(String inputText, int numRecommendations) {
        String[] processedInput = processText(inputText);
        HashMap<String, Float> articleScores = new HashMap<>();

        for (String id : tf.keySet()) {
            float score = calculateSimilarity(id, processedInput);
            articleScores.put(id, score);
        }

        return articleScores.entrySet().stream()
                .sorted((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()))
                .limit(numRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Loads the article from the json file
    public void loadArticlesFromJSON(String jsonFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFilePath))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonString.toString());
            for (String state : jsonObject.keySet()) {
                JSONArray newspapers = jsonObject.getJSONObject(state).getJSONArray("newspaper");
                for (int i = 0; i < newspapers.length(); i++) {
                    JSONObject newspaperJson = newspapers.getJSONObject(i);

                    String id = newspaperJson.getString("name"); // Article ID
                    String content = newspaperJson.optString("website", "") + " "
                            + newspaperJson.optString("media-class", "") + " "
                            + newspaperJson.optString("city-county-name", "") + " "
                            + newspaperJson.optString("extracted-from", "");

                    addArticle(id, content);
                }
            }

            calculateIDF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
