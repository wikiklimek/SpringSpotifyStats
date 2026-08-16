package com.spotify.wrapped.repository;

import com.spotify.wrapped.document.DailyStatsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyStatsRepository extends MongoRepository<DailyStatsDocument, String> {

    // Spring sam wygeneruje zapytanie do Mongo:
    // db.daily_stats.find({ spotifyId: "?", date: "?" })
    Optional<DailyStatsDocument> findBySpotifyIdAndDate(String spotifyId, LocalDate date);
}