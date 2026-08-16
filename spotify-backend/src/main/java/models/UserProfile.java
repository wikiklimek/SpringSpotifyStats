package models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    private String country;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private String email;
    
    @JsonProperty("explicit_content")
    private ExplicitContent explicitContent;
    
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    private Followers followers;
    private String href;
    private String id;
    private List<Image> images;
    private String product;
    private String type;
    private String uri;
    private String imageUrl;

    
    // Getters and Setters for UserProfile
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String display_name) {
        this.displayName = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExplicitContent getExplicitContent() {
        return explicitContent;
    }

    public void setExplicitContent(ExplicitContent explicit_content) {
        this.explicitContent = explicit_content;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls external_urls) {
        this.externalUrls = external_urls;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
        if (images != null && !images.isEmpty()) {
            this.imageUrl = images.get(0).getUrl(); 
        }
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
