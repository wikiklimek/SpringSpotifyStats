package com.spotify.wrapped.repository;

import com.spotify.wrapped.document.DailyTopTracksDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class DailyTopTracksRepositoryTest {

    @Autowired
    private DailyTopTracksRepository repository;

    @Test
    void shouldSaveAndFindTopTracksByDate() {
        // GIVEN
        LocalDate today = LocalDate.now();
        repository.save(new DailyTopTracksDocument("userX", today, null));

        // WHEN
        Optional<DailyTopTracksDocument> foundDoc = repository.findBySpotifyIdAndDate("userX", today);

        // THEN
        assertTrue(foundDoc.isPresent());

        // CLEANUP
        repository.deleteAll();
    }
}