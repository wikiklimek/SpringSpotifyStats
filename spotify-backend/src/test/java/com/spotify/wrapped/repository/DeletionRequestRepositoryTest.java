package com.spotify.wrapped.repository;

import com.spotify.wrapped.AbstractIntegrationTest;
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
@Transactional
class DeletionRequestRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private DeletionRequestRepository repository;

    @Test
    void shouldFindPendingRequestsBySpotifyId() {
        DeletionRequestEntity req = new DeletionRequestEntity("userTC", 30);
        req.setStatus(RequestStatus.PENDING);
        repository.save(req);

        Optional<DeletionRequestEntity> result = repository.findBySpotifyIdAndStatus("userTC", RequestStatus.PENDING);

        assertTrue(result.isPresent());
        assertEquals(30, result.get().getDaysToKeep());
    }

    @Test
    void shouldFindAllPendingRequestsOrderedByDate() {
        DeletionRequestEntity req1 = new DeletionRequestEntity("user1", 7);
        DeletionRequestEntity req2 = new DeletionRequestEntity("user2", 30);
        repository.save(req1);
        repository.save(req2);

        List<DeletionRequestEntity> results = repository.findAllByStatusOrderByRequestDateAsc(RequestStatus.PENDING);

        assertTrue(results.size() >= 2);
        assertEquals("user1", results.get(0).getSpotifyId());
    }
}