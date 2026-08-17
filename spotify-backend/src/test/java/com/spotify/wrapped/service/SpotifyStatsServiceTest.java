package com.spotify.wrapped.service;

import com.spotify.wrapped.model.Artist;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpotifyStatsServiceTest {

    private final SpotifyStatsService statsService = new SpotifyStatsService();

    @Test
    void shouldCalculateTopGenresCorrectly() {
        // GIVEN
        Artist artist1 = new Artist("id1", "Band 1", 0, List.of("metal", "rock"), null, null, null);
        Artist artist2 = new Artist("id2", "Band 2", 0, List.of("metal", "pop"), null, null, null);
        List<Artist> artists = List.of(artist1, artist2);

        // WHEN
        Map<String, Long> topGenres = statsService.calculateTopGenres(artists, 5);

        // THEN
        assertEquals(3, topGenres.size());
        assertEquals(2L, topGenres.get("metal"));
        assertEquals(1L, topGenres.get("rock"));
    }

    @Test
    void shouldReturnEmptyMapWhenArtistsListIsNull() {
        // WHEN
        Map<String, Long> result = statsService.calculateTopGenres(null, 5);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenArtistsListIsEmpty() {
        // WHEN
        Map<String, Long> result = statsService.calculateTopGenres(Collections.emptyList(), 5);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRespectLimitParameter() {
        // GIVEN
        Artist artist1 = new Artist("1", "1", 0, List.of("rock", "metal", "pop", "jazz"), null, null, null);

        // WHEN (Chcemy tylko TOP 2 gatunki)
        Map<String, Long> topGenres = statsService.calculateTopGenres(List.of(artist1), 2);

        // THEN
        assertEquals(2, topGenres.size(), "Metoda powinna zwrócić tylko 2 gatunki zgodnie z limitem");
    }
}