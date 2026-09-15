package com.spotify.wrapped.document;

import com.spotify.wrapped.model.Track;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "playback_history")
// fast duplicates check and user check
@CompoundIndex(name = "spotifyId_playedAt_idx", def = "{'spotifyId': 1, 'playedAt': -1}")
public class PlaybackHistoryDocument {

    @Id
    private String id;

    private String spotifyId;
    private Instant playedAt;
    private Track track;

    public PlaybackHistoryDocument() {}

    public PlaybackHistoryDocument(String spotifyId, Instant playedAt, Track track) {
        this.spotifyId = spotifyId;
        this.playedAt = playedAt;
        this.track = track;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public Instant getPlayedAt() { return playedAt; }
    public void setPlayedAt(Instant playedAt) { this.playedAt = playedAt; }
    public Track getTrack() { return track; }
    public void setTrack(Track track) { this.track = track; }
}
