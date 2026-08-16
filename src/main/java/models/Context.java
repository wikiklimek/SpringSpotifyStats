package models;

public  class Context {
    private String type;
    private String href;
    private ExternalUrls external_urls;
    private String uri;

    // Getters and Setters
    public String geType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public String geHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    
    public String geUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public ExternalUrls getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(ExternalUrls external_urls) {
        this.external_urls = external_urls;
    }
    
}
