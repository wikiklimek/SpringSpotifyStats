package models;

import java.util.List;

public class SpotifyTopResponseTrack extends SpotifyTopResponse {
private List<Track> items;
    
	public List<Track> getItems() {
        return items;
    }

    public void setItems(List<Track> items) {
        this.items = items;
    }
}
