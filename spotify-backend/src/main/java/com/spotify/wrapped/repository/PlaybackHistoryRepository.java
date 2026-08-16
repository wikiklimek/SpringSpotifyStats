package com.spotify.wrapped.repository;

import com.spotify.wrapped.document.PlaybackHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PlaybackHistoryRepository extends MongoRepository<PlaybackHistoryDocument, String> {

    // Spring sam zbuduje zapytanie sprawdzające, czy podany użytkownik ma w bazie wpis z dokładnie tą datą
    boolean existsBySpotifyIdAndPlayedAt(String spotifyId, Instant playedAt);

    // NOWA METODA: Usuń wszystko dla danego usera, co było słuchane PRZED daną datą
    void deleteBySpotifyIdAndPlayedAtBefore(String spotifyId, Instant dateBefore);
}