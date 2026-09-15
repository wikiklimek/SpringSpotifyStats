package com.spotify.wrapped.document;

import com.spotify.wrapped.model.Artist;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "daily_top_artists")
public class DailyTopArtistsDocument {
    @Id
    private String id;
    private String spotifyId;
    private LocalDate date;
    private List<Artist> topArtists;

    public DailyTopArtistsDocument() {}
    public DailyTopArtistsDocument(String spotifyId, LocalDate date, List<Artist> topArtists) {
        this.spotifyId = spotifyId; this.date = date; this.topArtists = topArtists;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<Artist> getTopArtists() { return topArtists; }
    public void setTopArtists(List<Artist> topArtists) { this.topArtists = topArtists; }
}