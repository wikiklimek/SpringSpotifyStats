package models;

import java.util.List;

public class SpotifyRecentlyResponse extends SpotifyResponseTopAndRecentlyAbstract {
	    private Cursors cursors;
	    private List<RecentlyItem> items;
	    
	 // Getters and Setters for SpotifyResponse
	    
	    public Cursors getCursors() {
	        return cursors;
	    }

	    public void setCursors(Cursors cursors) {
	        this.cursors = cursors;
	    }

	    public List<RecentlyItem> getItems() {
	        return items;
	    }

	    public void setItems(List<RecentlyItem> items) {
	        this.items = items;
	    }
}
