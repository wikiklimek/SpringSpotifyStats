package com.spotify.wrapped.repository;

import com.spotify.wrapped.AbstractIntegrationTest;
import com.spotify.wrapped.document.DailyTopTracksDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class DailyTopTracksRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private DailyTopTracksRepository repository;

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndFindTopTracksByDate() {
        LocalDate today = LocalDate.now();
        repository.save(new DailyTopTracksDocument("userX", today, null));

        Optional<DailyTopTracksDocument> foundDoc = repository.findBySpotifyIdAndDate("userX", today);

        assertTrue(foundDoc.isPresent());
    }
}