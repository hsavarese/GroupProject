package backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
// import nlp.ExtractDocument;
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
        Document doc = new Document("path", path).append("document", document);
        collection.insertOne(doc);
        System.out.println("Document uploaded to MongoDB successfully!");
    }
}

/* 



public class TextAnalyzer {

    private static final String PUNCTUATION = ",.;:/?!`\"\'";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please provide 3 arguments: <textFileToAnalyze.txt> <char freq output file> <word freq output file>");
            return;
        }

        String fileToAnalyze = args[0];
        System.out.println("Character Count: " + countCharacters(fileToAnalyze));
        System.out.println("Word Count: " + countWords(fileToAnalyze));
        System.out.println("Line Count: " + countLines(fileToAnalyze));

        writeCharacterFrequency(countCharacterFrequency(fileToAnalyze), args[1]);
        writeWordFrequency(countWordFrequency(fileToAnalyze), args[2]);
    }

    public static int countCharacters(String fileName) {
        String contents = getContents(fileName);
        return contents.length();
    }

    public static int countWords(String fileName) {
        int wordCount = 0;
        String contents = getContents(fileName);

        for (int i = 1; i < contents.length(); i++) {
            if (isSeparator(contents.charAt(i - 1)) && !isSeparator(contents.charAt(i))) {
                wordCount++;
            }
        }

        if (!contents.isEmpty() && !isSeparator(contents.charAt(0))) {
            wordCount++;
        }

        return wordCount;
    }

    public static int countLines(String fileName) {
        int lineCount = 0;
        String contents = getContents(fileName);

        for (char character : contents.toCharArray()) {
            if (character == '\n') {
                lineCount++;
            }
        }

        return lineCount;
    }

    public static HashMap<Character, Integer> countCharacterFrequency(String fileName) {
        HashMap<Character, Integer> characterFrequency = new HashMap<>();
        String contents = getContents(fileName);

        for (char ch : contents.toCharArray()) {
            characterFrequency.put(ch, characterFrequency.getOrDefault(ch, 0) + 1);
        }

        return characterFrequency;
    }

    public static HashMap<String, Integer> countWordFrequency(String fileName) {
        HashMap<String, Integer> wordFrequency = new HashMap<>();
        String contents = getContents(fileName);
        StringBuilder currentWord = new StringBuilder();

        for (char ch : contents.toCharArray()) {
            if (isSeparator(ch)) {
                if (currentWord.length() > 0) {
                    String word = currentWord.toString();
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                    currentWord.setLength(0);
                }
            } else {
                currentWord.append(ch);
            }
        }

        if (currentWord.length() > 0) {
            String word = currentWord.toString();
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        return wordFrequency;
    }

    public static void writeCharacterFrequency(HashMap<Character, Integer> charFrequency, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Character ch : charFrequency.keySet()) {
                writer.write(ch + "," + charFrequency.get(ch) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
            e.printStackTrace();
        }
    }

    public static void writeWordFrequency(HashMap<String, Integer> wordFrequency, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String word : wordFrequency.keySet()) {
                writer.write(word + "," + wordFrequency.get(word) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
            e.printStackTrace();
        }
    }

    private static String getContents(String fileName) {
        StringBuilder content = new StringBuilder();
        try (FileReader fileReader = new FileReader(fileName)) {
            int c;
            while ((c = fileReader.read()) != -1) {
                content.append((char) c);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
        return content.toString();
    }

    private static boolean isSeparator(char ch) {
        return ch == ' ' || ch == '\n' || PUNCTUATION.indexOf(ch) != -1;
    }
} */