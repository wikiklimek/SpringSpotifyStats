package models;

public class SpotifyCurrentlyPlaying {
    private long timestamp;
    private Context context;
    private int progress_ms;
    private Track item;
    private String currently_playing_type;
    private Actions actions;
    private boolean is_playing;

    // Getters and Setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getProgress_ms() {
        return progress_ms;
    }

    public void setProgress_ms(int progress_ms) {
        this.progress_ms = progress_ms;
    }

    public Track getItem() {
        return item;
    }

    public void setItem(Track item) {
        this.item = item;
    }

    public String getCurrently_playing_type() {
        return currently_playing_type;
    }

    public void setCurrently_playing_type(String currently_playing_type) {
        this.currently_playing_type = currently_playing_type;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public boolean isIs_playing() {
        return is_playing;
    }

    public void setIs_playing(boolean is_playing) {
        this.is_playing = is_playing;
    }

    
}