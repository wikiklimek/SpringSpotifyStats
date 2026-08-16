package com.spotify.wrapped.document;


import com.spotify.wrapped.model.Artist;
import com.spotify.wrapped.model.Track;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "daily_stats")
public class DailyStatsDocument {

    @Id
    private String id; // Automatycznie generowane przez Mongo

    private String spotifyId; // ID użytkownika (żeby wiedzieć, czyje to statystyki)

    private LocalDate date; // Data pobrania (np. dzisiaj)

    // Spring Data Mongo automatycznie zserializuje te listy do JSON-a!
    private List<Track> topTracks;
    private List<Artist> topArtists;

    public DailyStatsDocument() {}

    public DailyStatsDocument(String spotifyId, LocalDate date, List<Track> topTracks, List<Artist> topArtists) {
        this.spotifyId = spotifyId;
        this.date = date;
        this.topTracks = topTracks;
        this.topArtists = topArtists;
    }

    // --- Gettery i Settery ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<Track> getTopTracks() { return topTracks; }
    public void setTopTracks(List<Track> topTracks) { this.topTracks = topTracks; }
    public List<Artist> getTopArtists() { return topArtists; }
    public void setTopArtists(List<Artist> topArtists) { this.topArtists = topArtists; }
}