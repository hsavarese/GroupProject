package nlp;

import org.bson.Document;

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

    public Document getDocument() {
        Document document = new Document();
        document.append("name", name)
                .append("video", video)
                .append("twitter", twitter)
                .append("website", website)
                .append("facebook", facebook)
                .append("instagram", instagram)
                .append("youtube", youtube)
                .append("wikipedia", wikipedia)
                .append("city-county-name", cityCountyName)
                .append("us-state", usState)
                .append("extracted-from", extractedFrom);
        return document;
    }
}
