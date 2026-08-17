package com.spotify.wrapped.service;

import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.RequestStatus;
import com.spotify.wrapped.repository.DeletionRequestRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Pozwala na używanie @Mock
class PrivacyServiceTest {

    @Mock
    private DeletionRequestRepository requestRepository;

    @Mock
    private PlaybackHistoryRepository playbackHistoryRepository;

    @InjectMocks
    private PrivacyService privacyService;

    @Test
    void shouldCreateNewDeletionRequestIfNotExists() {
        // GIVEN
        String spotifyId = "user123";
        when(requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING)).thenReturn(Optional.empty());

        // WHEN
        String result = privacyService.createDeletionRequest(spotifyId, 30);

        // THEN
        assertEquals("Prośba o usunięcie danych starszych niż 30 dni została wysłana.", result);
        verify(requestRepository, times(1)).save(any(DeletionRequestEntity.class)); // Upewniamy się, że baza próbowała zapisać
    }

    @Test
    void shouldNotCreateRequestIfPendingExists() {
        // GIVEN
        String spotifyId = "user123";
        when(requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING))
                .thenReturn(Optional.of(new DeletionRequestEntity(spotifyId, 30)));

        // WHEN
        String result = privacyService.createDeletionRequest(spotifyId, 7);

        // THEN
        assertEquals("Masz już oczekującą prośbę. Poczekaj na decyzję administratora lub ją wycofaj.", result);
        verify(requestRepository, never()).save(any()); // Upewniamy się, że NIC nie zapisano
    }

    @Test
    void shouldApproveRequestAndCallDeletion() {
        // GIVEN
        Long reqId = 1L;
        DeletionRequestEntity req = new DeletionRequestEntity("user123", 7);
        when(requestRepository.findById(reqId)).thenReturn(Optional.of(req));
        when(playbackHistoryRepository.deleteBySpotifyIdAndPlayedAtBefore(eq("user123"), any())).thenReturn(150L);

        // WHEN
        long deletedDocs = privacyService.approveRequest(reqId);

        // THEN
        assertEquals(150L, deletedDocs);
        assertEquals(RequestStatus.APPROVED, req.getStatus());
        verify(requestRepository, times(1)).save(req);
    }
}