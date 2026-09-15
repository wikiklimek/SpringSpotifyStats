package com.spotify.wrapped.document;

import com.spotify.wrapped.model.Track;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "daily_top_tracks")
public class DailyTopTracksDocument {
    @Id
    private String id;
    private String spotifyId;
    private LocalDate date;
    private List<Track> topTracks;

    public DailyTopTracksDocument() {}
    public DailyTopTracksDocument(String spotifyId, LocalDate date, List<Track> topTracks) {
        this.spotifyId = spotifyId; this.date = date; this.topTracks = topTracks;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<Track> getTopTracks() { return topTracks; }
    public void setTopTracks(List<Track> topTracks) { this.topTracks = topTracks; }
}