package com.spotify.wrapped.controller;

import com.spotify.wrapped.document.PlaybackHistoryDocument;
import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.model.Track;
import com.spotify.wrapped.repository.DailyTopArtistsRepository;
import com.spotify.wrapped.repository.DailyTopTracksRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PlaybackSyncService;
import com.spotify.wrapped.service.PrivacyService;
import com.spotify.wrapped.service.SpotifyClientService;
import com.spotify.wrapped.service.SpotifyStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpotifyControllerTest {

    @Mock private SpotifyClientService spotifyService;
    @Mock private UserRepository userRepository;
    @Mock private DailyTopTracksRepository topTracksRepository;
    @Mock private DailyTopArtistsRepository topArtistsRepository;
    @Mock private SpotifyStatsService statsService;
    @Mock private PlaybackSyncService playbackSyncService;
    @Mock private PrivacyService privacyService;
    @Mock private PlaybackHistoryRepository playbackHistoryRepository;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUpSecurityContext() {
        // Zmuszamy Springa by myślał, że jesteśmy zalogowani
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttribute("id")).thenReturn("mock-user-123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldRequestDeletionSuccessfully() {
        // GIVEN
        when(privacyService.createDeletionRequest("mock-user-123", 7)).thenReturn("OK");

        // WHEN
        Map<String, String> response = spotifyController.requestDeletion(7);

        // THEN
        assertEquals("OK", response.get("message"));
    }

    @Test
    void shouldWithdrawDeletionRequestSuccessfully() {
        // GIVEN
        when(privacyService.withdrawRequest("mock-user-123")).thenReturn("COFNIĘTO");

        // WHEN
        Map<String, String> response = spotifyController.withdrawDeletionRequest();

        // THEN
        assertEquals("COFNIĘTO", response.get("message"));
    }

    @Test
    void shouldGetHistory() {
        // GIVEN
        PlaybackHistoryDocument doc = new PlaybackHistoryDocument("mock-user-123", Instant.now(), new Track(null, null, 0, 0, null, null, null, null, false));
        when(playbackHistoryRepository.findAllBySpotifyIdOrderByPlayedAtDesc("mock-user-123"))
                .thenReturn(List.of(doc));

        // WHEN
        List<PlaybackHistoryDocument> history = spotifyController.getHistory();

        // THEN
        assertEquals(1, history.size());
        assertEquals("mock-user-123", history.get(0).getSpotifyId());
    }

    @Test
    void shouldGetPrivacyStatus() {
        // GIVEN
        DeletionRequestEntity req = new DeletionRequestEntity("mock-user-123", 30);
        when(privacyService.getPendingRequests()).thenReturn(List.of(req));

        // WHEN
        DeletionRequestEntity status = spotifyController.getPrivacyStatus();

        // THEN
        assertNotNull(status);
        assertEquals("mock-user-123", status.getSpotifyId());
        assertEquals(30, status.getDaysToKeep());
    }
}