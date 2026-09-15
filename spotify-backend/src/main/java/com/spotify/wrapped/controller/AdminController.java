package com.spotify.wrapped.controller;

import com.spotify.wrapped.document.DailyTopArtistsDocument;
import com.spotify.wrapped.document.DailyTopTracksDocument;
import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.DailyTopArtistsRepository;
import com.spotify.wrapped.repository.DailyTopTracksRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PrivacyService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PrivacyService privacyService;
    private final DailyTopTracksRepository tracksRepo;
    private final DailyTopArtistsRepository artistsRepo;
    private final PlaybackHistoryRepository historyRepo;

    public AdminController(UserRepository userRepository, PrivacyService privacyService,
                           DailyTopTracksRepository tracksRepo, DailyTopArtistsRepository artistsRepo, PlaybackHistoryRepository historyRepo) {
        this.userRepository = userRepository;
        this.privacyService = privacyService;
        this.tracksRepo = tracksRepo;
        this.artistsRepo = artistsRepo;
        this.historyRepo = historyRepo;
    }

    @GetMapping("/requests")
    public List<DeletionRequestEntity> getPendingRequestsJson() {
        return privacyService.getPendingRequests();
    }

    @PostMapping("/request/{id}/approve")
    public Map<String, Long> approveRequest(@PathVariable Long id) {
        long deletedCount = privacyService.approveRequest(id);
        return Map.of("deletedEntries", deletedCount);
    }

    @PostMapping("/request/{id}/reject")
    public void rejectRequest(@PathVariable Long id) {
        privacyService.rejectRequest(id);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            long trackDocs = tracksRepo.countBySpotifyId(user.getSpotifyId());
            long artistDocs = artistsRepo.countBySpotifyId(user.getSpotifyId());
            return Map.<String, Object>of(
                    "spotifyId", user.getSpotifyId(),
                    "displayName", user.getDisplayName(),
                    "email", user.getEmail() != null ? user.getEmail() : "Brak",
                    "totalDocs", trackDocs + artistDocs
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/users/{spotifyId}/docs")
    public Map<String, Object> getUserDocs(@PathVariable String spotifyId) {
        List<DailyTopTracksDocument> tracks = tracksRepo.findAllBySpotifyId(spotifyId);
        List<DailyTopArtistsDocument> artists = artistsRepo.findAllBySpotifyId(spotifyId);
        return Map.of("tracks", tracks, "artists", artists);
    }

    @DeleteMapping("/records")
    public Map<String, Long> deleteRecords(@RequestParam int days, @RequestParam(required = false) String spotifyId) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        //Instant cutoffInstant = Instant.now().minus(days, ChronoUnit.DAYS);

        long deletedTracks = 0, deletedArtists = 0 /*, deletedHistory = 0*/;

        if (spotifyId != null && !spotifyId.isEmpty()) {
            deletedTracks = tracksRepo.deleteBySpotifyIdAndDateBefore(spotifyId, cutoffDate);
            deletedArtists = artistsRepo.deleteBySpotifyIdAndDateBefore(spotifyId, cutoffDate);
            //deletedHistory = historyRepo.deleteBySpotifyIdAndPlayedAtBefore(spotifyId, cutoffInstant);
        }
        else
        {
            deletedTracks = tracksRepo.deleteByDateBefore(cutoffDate);
            deletedArtists = artistsRepo.deleteByDateBefore(cutoffDate);
            //deletedHistory = historyRepo.deleteByPlayedAtBefore(cutoffInstant);
        }

        return Map.of("deletedEntries", deletedTracks + deletedArtists /*+ deletedHistory*/);
    }
}