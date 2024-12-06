package nlp;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class Newspaper {

    private String name;
    private String video;
    private String twitter;
    private String website;
    private String facebook;
    private String instagram;
    private String youtube;
    private String wikipedia;
    private String cityCountyName;
    private String usState;
    private String extractedFrom;

    // Constructor
    public Newspaper(String name, String video, String twitter, String website, String facebook,
                     String instagram, String youtube, String wikipedia, String cityCountyName, 
                     String usState, String extractedFrom) {
        this.name = name;
        this.video = video;
        this.twitter = twitter;
        this.website = website;
        this.facebook = facebook;
        this.instagram = instagram;
        this.youtube = youtube;
        this.wikipedia = wikipedia;
        this.cityCountyName = cityCountyName;
        this.usState = usState;
        this.extractedFrom = extractedFrom;
    }

    // Convert Newspaper object to Document
    public Document getDocument() {
        return new Document()
                .append("name", name)
                .append("video", video)
                .append("twitter", twitter)
                .append("website", website)
                .append("facebook", facebook)
                .append("instagram", instagram)
                .append("youtube", youtube)
                .append("wikipedia", wikipedia)
                .append("cityCountyName", cityCountyName)
                .append("usState", usState)
                .append("extractedFrom", extractedFrom);
    }

    // Method to process text fields
    public void processTextFields() {
        this.name = processText(name);
        this.extractedFrom = processText(extractedFrom);
        // You can process other fields as needed, like twitter, facebook, etc.
    }

    // Example of text processing method that might use 'twextword'
    private String processText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Apply twextword processing to each word
        String[] words = text.split("\\s+");
        List<String> processedWords = new ArrayList<>();
        
        for (String word : words) {
            processedWords.add(twextword(word));  // Apply twextword method to each word
        }

        return String.join(" ", processedWords);
    }

    // Example method for twextword, you can modify this logic as needed
    private String twextword(String word) {
        // Example processing, could be removing unwanted characters or transforming text
        return word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();  // Removes non-alphanumeric characters and makes lowercase
    }
}
