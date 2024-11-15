package backend;
import org.bson.Document;
import java.util.Map;

public class Article {
// This is every single way that the article is indetified with
    private String name;
    private String website;
    private String mediaClass;
    private String mediaSubclass;
    private String usState;
    private String cityCountyName;
    private Map<String, String> socialLinks;
    private String extractedFrom;

    public Article(String name, String website, String mediaClass, String mediaSubclass,
                   String usState, String cityCountyName,
                   Map<String, String> socialLinks, String extractedFrom) {
        this.name = name;
        this.website = website;
        this.mediaClass = mediaClass;
        this.mediaSubclass = mediaSubclass;
        this.usState = usState;
        this.cityCountyName = cityCountyName;
        this.socialLinks = socialLinks;
        this.extractedFrom = extractedFrom;
    }

    
    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getMediaClass() {
        return mediaClass;
    }

    public String getMediaSubclass() {
        return mediaSubclass;
    }

    public String getUsState() {
        return usState;
    }

    public String getCityCountyName() {
        return cityCountyName;
    }

    public Map<String, String> getSocialLinks() {
        return socialLinks;
    }

    public String getExtractedFrom() {
        return extractedFrom;
    }

    // for the mongodb document changes
    public Document getDocument() {
        Document document = new Document();
        document.append("name", name);
        document.append("website", website);
        document.append("media-class", mediaClass);
        document.append("media-subclass", mediaSubclass);
        document.append("us-state", usState);
        document.append("city-county-name", cityCountyName);
        document.append("social-links", socialLinks);
        document.append("extracted-from", extractedFrom);
        return document;
    }

}
