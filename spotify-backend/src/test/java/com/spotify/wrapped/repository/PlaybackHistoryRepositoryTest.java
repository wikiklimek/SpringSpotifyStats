package com.spotify.wrapped.repository;

import com.spotify.wrapped.AbstractIntegrationTest;
import com.spotify.wrapped.document.PlaybackHistoryDocument;
import com.spotify.wrapped.model.Track;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PlaybackHistoryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PlaybackHistoryRepository repository;

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndFindHistoryOrderedByDate() {
        Track track1 = new Track("1", "Starsza piosenka", 50, 200, null, null, null, null, false);
        Track track2 = new Track("2", "Nowsza piosenka", 80, 180, null, null, null, null, false);

        repository.save(new PlaybackHistoryDocument("user123", Instant.parse("2026-08-10T10:00:00Z"), track1));
        repository.save(new PlaybackHistoryDocument("user123", Instant.parse("2026-08-17T10:00:00Z"), track2));

        List<PlaybackHistoryDocument> results = repository.findAllBySpotifyIdOrderByPlayedAtDesc("user123");

        assertEquals(2, results.size());
        assertEquals("Nowsza piosenka", results.get(0).getTrack().name());
        assertEquals("Starsza piosenka", results.get(1).getTrack().name());
    }

    @Test
    void shouldCheckIfPlaybackExists() {
        Instant time = Instant.now();
        repository.save(new PlaybackHistoryDocument("wika", time, null));

        assertTrue(repository.existsBySpotifyIdAndPlayedAt("wika", time));
        assertFalse(repository.existsBySpotifyIdAndPlayedAt("haker", time));
    }
}