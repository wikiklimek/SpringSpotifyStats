package com.spotify.wrapped.service;

import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.RequestStatus;
import com.spotify.wrapped.repository.DeletionRequestRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PrivacyService {

    private final DeletionRequestRepository requestRepository;
    private final PlaybackHistoryRepository playbackHistoryRepository;

    public PrivacyService(DeletionRequestRepository requestRepository, PlaybackHistoryRepository playbackHistoryRepository) {
        this.requestRepository = requestRepository;
        this.playbackHistoryRepository = playbackHistoryRepository;
    }


    public String createDeletionRequest(String spotifyId, int daysToKeep) {

        if (requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING).isPresent()) {
            return "Masz już oczekującą prośbę. Poczekaj na decyzję administratora lub ją wycofaj.";
        }

        DeletionRequestEntity newRequest = new DeletionRequestEntity(spotifyId, daysToKeep);
        requestRepository.save(newRequest);
        return "Prośba o usunięcie danych starszych niż " + daysToKeep + " dni została wysłana.";
    }

    public String withdrawRequest(String spotifyId) {
        var pendingReq = requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING);
        if (pendingReq.isPresent()) {
            DeletionRequestEntity request = pendingReq.get();
            request.setStatus(RequestStatus.WITHDRAWN); //no detele, just withdraw
            requestRepository.save(request);
            return "Prośba została wycofana. Administrator jej nie zobaczy.";
        }
        return "Brak oczekującej prośby do wycofania.";
    }


    public List<DeletionRequestEntity> getPendingRequests() {
        return requestRepository.findAllByStatusOrderByRequestDateAsc(RequestStatus.PENDING);
    }

    //TRANSACTIONAL!!! no mess in sql
    @Transactional
    public long approveRequest(Long requestId) {
        DeletionRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono prośby."));

        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        Instant cutoffDate = Instant.now().minus(request.getDaysToKeep(), ChronoUnit.DAYS);

        return playbackHistoryRepository.deleteBySpotifyIdAndPlayedAtBefore(request.getSpotifyId(), cutoffDate);
    }

    @Transactional
    public void rejectRequest(Long requestId) {
        DeletionRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono prośby."));
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        System.out.println("INFO: Odrzucono prośbę usunięcia dla usera: " + request.getSpotifyId());
    }
}