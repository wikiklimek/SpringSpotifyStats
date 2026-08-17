package com.spotify.wrapped.service;

import com.spotify.wrapped.model.Artist;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotifyStatsServiceTest {

    private final SpotifyStatsService statsService = new SpotifyStatsService();

    @Test
    void shouldCalculateTopGenresCorrectly() {
        // GIVEN (przygotowanie danych)
        // Zmień linijki GIVEN w SpotifyStatsServiceTest na:
        Artist artist1 = new Artist("id1", "Band 1", 0, List.of("metal", "rock"), null, null, null);
        Artist artist2 = new Artist("id2", "Band 2", 0, List.of("metal", "pop"), null, null, null);
        List<Artist> artists = List.of(artist1, artist2);

        // WHEN (wykonanie logiki)
        Map<String, Long> topGenres = statsService.calculateTopGenres(artists, 5);

        // THEN (sprawdzenie wyniku)
        assertEquals(2L, topGenres.get("metal"));
        assertEquals(1L, topGenres.get("rock"));
        assertEquals(1L, topGenres.get("pop"));
    }
}