package nlp;

import backend.DatabaseManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExtractDocument {

    public static Map<String, Integer> extractdocument(String fileName) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            String[] words = content.toLowerCase().split("[^\\w']+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return frequencyMap;
    }

    public static void writeWordFrequency(String inputFile) {
        // Create an instance of DatabaseManager to handle MongoDB operations
        DatabaseManager dbManager = new DatabaseManager();

        Map<String, Integer> freqOutput = extractdocument(inputFile);
    
        // Upload word frequencies to MongoDB
        dbManager.uploadWordFrequencies(freqOutput, inputFile);
    }
    
}
