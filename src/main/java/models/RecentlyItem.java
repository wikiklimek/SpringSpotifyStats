package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecentlyItem {
        private Track track;
        
        @JsonProperty("played_at")
        private String playedAt;
        
        private Context context;

        // Getters and Setters
        public Track getTrack() {
            return track;
        }

        public void setTrack(Track track) {
            this.track = track;
        }

        public String getPlayedAt() {
            return playedAt;
        }

        public void setPlayedAt(String played_at) {
            this.playedAt = played_at;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
}
