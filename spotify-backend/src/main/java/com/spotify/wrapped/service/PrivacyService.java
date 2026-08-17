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

    // --- METODY DLA UŻYTKOWNIKA ---

    public String createDeletionRequest(String spotifyId, int daysToKeep) {
        // Sprawdzamy, czy użytkownik nie ma już aktywnej prośby
        boolean hasPending = requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING).isPresent();
        if (hasPending) {
            return "Masz już oczekującą prośbę. Poczekaj na decyzję administratora lub ją wycofaj.";
        }

        // Zapisujemy nową prośbę w PostgreSQL
        DeletionRequestEntity newRequest = new DeletionRequestEntity(spotifyId, daysToKeep);
        requestRepository.save(newRequest);
        return "Prośba o usunięcie danych starszych niż " + daysToKeep + " dni została wysłana.";
    }

    public String withdrawRequest(String spotifyId) {
        var pendingReq = requestRepository.findBySpotifyIdAndStatus(spotifyId, RequestStatus.PENDING);
        if (pendingReq.isPresent()) {
            DeletionRequestEntity request = pendingReq.get();
            request.setStatus(RequestStatus.WITHDRAWN); // Zmieniamy status
            requestRepository.save(request); // Aktualizujemy w bazie SQL
            return "Prośba została wycofana. Administrator jej nie zobaczy.";
        }
        return "Brak oczekującej prośby do wycofania.";
    }

    // --- METODY DLA ADMINISTRATORA ---

    public List<DeletionRequestEntity> getPendingRequests() {
        return requestRepository.findAllByStatusOrderByRequestDateAsc(RequestStatus.PENDING);
    }

    // @Transactional gwarantuje, że jeśli coś zepsuje się po drodze, zmiany w SQL zostaną cofnięte
    @Transactional
    public long approveRequest(Long requestId) {
        DeletionRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono prośby."));

        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        Instant cutoffDate = Instant.now().minus(request.getDaysToKeep(), ChronoUnit.DAYS);
        // Zwracamy ilość skasowanych rekordów z bazy!
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