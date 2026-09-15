package com.spotify.wrapped.service;

import com.spotify.wrapped.model.PlayHistoryItem;
import com.spotify.wrapped.model.Track;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaybackSyncServiceTest {

    @Mock
    private SpotifyClientService spotifyClientService;

    @Mock
    private PlaybackHistoryRepository playbackHistoryRepository;

    @InjectMocks
    private PlaybackSyncService playbackSyncService;

    @Test
    void shouldSyncOnlyNewPlaybacks() {
        // GIVEN
        String token = "fake-token";
        String spotifyId = "user1";

        Track track1 = new Track("t1", "Song A", 50, 2000, null, null, null, null, false);
        Track track2 = new Track("t2", "Song B", 50, 2000, null, null, null, null, false);

        PlayHistoryItem item1 = new PlayHistoryItem(track1, Instant.parse("2026-08-17T10:00:00Z"));
        PlayHistoryItem item2 = new PlayHistoryItem(track2, Instant.parse("2026-08-17T11:00:00Z"));

        when(spotifyClientService.getRecentlyPlayed(token)).thenReturn(List.of(item1, item2));

        when(playbackHistoryRepository.existsBySpotifyIdAndPlayedAt(spotifyId, item1.playedAt())).thenReturn(true);
        when(playbackHistoryRepository.existsBySpotifyIdAndPlayedAt(spotifyId, item2.playedAt())).thenReturn(false);

        // WHEN
        int added = playbackSyncService.syncRecentPlaybacks(token, spotifyId);

        // THEN
        assertEquals(1, added); // Zapisano tylko jeden nowy utwór!
        verify(playbackHistoryRepository, times(1)).saveAll(anyList());
    }
}