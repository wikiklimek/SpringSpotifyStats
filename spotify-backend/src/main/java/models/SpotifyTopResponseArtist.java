package models;

import java.util.List;

public class SpotifyTopResponseArtist extends SpotifyTopResponse {
    private List<Artist> items;
    
	public List<Artist> getItems() {
        return items;
    }

    public void setItems(List<Artist> items) {
        this.items = items;
    }
}
