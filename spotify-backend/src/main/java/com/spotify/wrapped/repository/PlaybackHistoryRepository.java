package com.spotify.wrapped.repository;

import com.spotify.wrapped.document.PlaybackHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PlaybackHistoryRepository extends MongoRepository<PlaybackHistoryDocument, String> {
    boolean existsBySpotifyIdAndPlayedAt(String spotifyId, Instant playedAt);

    long deleteBySpotifyIdAndPlayedAtBefore(String spotifyId, Instant dateBefore);
    long deleteByPlayedAtBefore(Instant dateBefore);

    List<PlaybackHistoryDocument> findAllBySpotifyIdOrderByPlayedAtDesc(String spotifyId);
}