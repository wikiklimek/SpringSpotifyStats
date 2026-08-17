package com.spotify.wrapped.repository;

import com.spotify.wrapped.document.DailyTopArtistsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyTopArtistsRepository extends MongoRepository<DailyTopArtistsDocument, String> {
    Optional<DailyTopArtistsDocument> findBySpotifyIdAndDate(String spotifyId, LocalDate date);
    List<DailyTopArtistsDocument> findAllBySpotifyId(String spotifyId);
    long countBySpotifyId(String spotifyId);
    long deleteBySpotifyIdAndDateBefore(String spotifyId, LocalDate date);
    long deleteByDateBefore(LocalDate date);
}