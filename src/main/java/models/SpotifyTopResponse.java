package models;


public abstract class SpotifyTopResponse extends SpotifyResponseTopAndRecentlyAbstract {

    private int offset;
    private String previous;

    // Getters and Setters
    
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }


  
}
