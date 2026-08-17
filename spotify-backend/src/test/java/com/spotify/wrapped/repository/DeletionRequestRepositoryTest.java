package com.spotify.wrapped.repository;

import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional // Gwarantuje, że po każdym teście baza zostanie wyczyszczona (Rollback)!
class DeletionRequestRepositoryTest {

    @Autowired
    private DeletionRequestRepository repository;

    @Test
    void shouldFindPendingRequestsBySpotifyId() {
        // GIVEN: Tworzymy prośbę w bazie
        DeletionRequestEntity req = new DeletionRequestEntity("userX", 30);
        req.setStatus(RequestStatus.PENDING);
        repository.save(req);

        // WHEN
        Optional<DeletionRequestEntity> result = repository.findBySpotifyIdAndStatus("userX", RequestStatus.PENDING);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(30, result.get().getDaysToKeep());
    }

    @Test
    void shouldFindAllPendingRequestsOrderedByDate() {
        // GIVEN
        DeletionRequestEntity req1 = new DeletionRequestEntity("user1", 7);
        DeletionRequestEntity req2 = new DeletionRequestEntity("user2", 30);
        repository.save(req1);
        repository.save(req2);

        // WHEN
        List<DeletionRequestEntity> results = repository.findAllByStatusOrderByRequestDateAsc(RequestStatus.PENDING);

        // THEN
        assertTrue(results.size() >= 2);
        // Sprawdzamy czy pierwszy na liście jest najstarszy wpis (zachowanie kolejności)
        assertEquals("user1", results.get(0).getSpotifyId());
    }
}