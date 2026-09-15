package com.spotify.wrapped.controller;

import com.spotify.wrapped.document.DailyTopArtistsDocument;
import com.spotify.wrapped.document.DailyTopTracksDocument;
import com.spotify.wrapped.document.PlaybackHistoryDocument;
import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.model.Artist;
import com.spotify.wrapped.model.CurrentlyPlayingResponse;
import com.spotify.wrapped.model.Track;
import com.spotify.wrapped.repository.DailyTopArtistsRepository;
import com.spotify.wrapped.repository.DailyTopTracksRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PlaybackSyncService;
import com.spotify.wrapped.service.PrivacyService;
import com.spotify.wrapped.service.SpotifyClientService;
import com.spotify.wrapped.service.SpotifyStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SpotifyController {

    private final SpotifyClientService spotifyService;
    private final UserRepository userRepository;
    private final DailyTopTracksRepository topTracksRepository;
    private final DailyTopArtistsRepository topArtistsRepository;
    private final SpotifyStatsService statsService;
    private final PlaybackSyncService playbackSyncService;
    private final PrivacyService privacyService;
    private final PlaybackHistoryRepository playbackHistoryRepository;

    public SpotifyController(SpotifyClientService spotifyService, UserRepository userRepository,
                             DailyTopTracksRepository topTracksRepository, DailyTopArtistsRepository topArtistsRepository,
                             SpotifyStatsService statsService, PlaybackSyncService playbackSyncService,
                             PrivacyService privacyService, PlaybackHistoryRepository playbackHistoryRepository) {
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
        this.topTracksRepository = topTracksRepository;
        this.topArtistsRepository = topArtistsRepository;
        this.statsService = statsService;
        this.playbackSyncService = playbackSyncService;
        this.privacyService = privacyService;
        this.playbackHistoryRepository = playbackHistoryRepository;
    }

    // spotify ID from RAm
    private String getSafeSpotifyIdFromSession() {
        org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (auth != null &&
                auth.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
            return oauth2User.getAttribute("id");
        }
        throw new IllegalArgumentException("Brak sesji użytkownika");
    }

    private String getAndSaveUserSpotifyId(OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        var userProfile = spotifyService.getUserProfile(token);
        String spotifyId = userProfile.id();

        Optional<UserEntity> existingUser = userRepository.findBySpotifyId(spotifyId);
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setLastLoginDate(LocalDate.now());
            userRepository.save(user);
        } else {
            userRepository.save(new UserEntity(spotifyId, userProfile.displayName(), userProfile.email(), LocalDate.now()));
        }
        return spotifyId;
    }


    @GetMapping("/api/top-tracks")
    public List<Track> getTopTracks(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = getAndSaveUserSpotifyId(authorizedClient);
        LocalDate today = LocalDate.now();

        return topTracksRepository.findBySpotifyIdAndDate(spotifyId, today).map(DailyTopTracksDocument::getTopTracks).orElseGet(() -> {
            List<Track> tracks = spotifyService.getTopTracks(token, 10);
            topTracksRepository.save(new DailyTopTracksDocument(spotifyId, today, tracks));
            return tracks;
        });
    }

    @GetMapping("/api/top-artists")
    public List<Artist> getTopArtists(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = getAndSaveUserSpotifyId(authorizedClient);
        LocalDate today = LocalDate.now();

        return topArtistsRepository.findBySpotifyIdAndDate(spotifyId, today).map(DailyTopArtistsDocument::getTopArtists).orElseGet(() -> {
            List<Artist> artists = spotifyService.getTopArtists(token, 10);
            topArtistsRepository.save(new DailyTopArtistsDocument(spotifyId, today, artists));
            return artists;
        });
    }

    @GetMapping("/api/top-genres")
    public Map<String, Long> getTopGenres(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        List<Artist> topArtists = getTopArtists(authorizedClient);
        return statsService.calculateTopGenres(topArtists, 10);
    }

    @GetMapping("/api/currently-playing")
    public org.springframework.http.ResponseEntity<?> getCurrentlyPlaying(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        CurrentlyPlayingResponse response = spotifyService.getCurrentlyPlaying(token);
        if (response == null || response.item() == null) {
            return org.springframework.http.ResponseEntity.noContent().build(); // 204
        }
        return org.springframework.http.ResponseEntity.ok(response);
    }

    @GetMapping("/api/recently-played/sync")
    public Map<String, String> syncRecentlyPlayed(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = getAndSaveUserSpotifyId(authorizedClient);
        int added = playbackSyncService.syncRecentPlaybacks(token, spotifyId);
        return Map.of("message", "Zsynchronizowano pomyślnie. Nowe utwory: " + added);
    }


    @PostMapping("/api/privacy/request-deletion")
    public Map<String, String> requestDeletion(@RequestParam(defaultValue = "7") int days) {
        String spotifyId = getSafeSpotifyIdFromSession();
        return Map.of("message", privacyService.createDeletionRequest(spotifyId, days));
    }

    @PostMapping("/api/privacy/withdraw")
    public Map<String, String> withdrawDeletionRequest() {
        String spotifyId = getSafeSpotifyIdFromSession();
        return Map.of("message", privacyService.withdrawRequest(spotifyId));
    }

    @GetMapping("/api/history")
    public List<PlaybackHistoryDocument> getHistory() {
        String spotifyId = getSafeSpotifyIdFromSession();
        return playbackHistoryRepository.findAllBySpotifyIdOrderByPlayedAtDesc(spotifyId);
    }

    @GetMapping("/api/privacy/status")
    public DeletionRequestEntity getPrivacyStatus() {
        String spotifyId = getSafeSpotifyIdFromSession();
        return privacyService.getPendingRequests().stream()
                .filter(req -> req.getSpotifyId().equals(spotifyId))
                .findFirst()
                .orElse(null);
    }
}