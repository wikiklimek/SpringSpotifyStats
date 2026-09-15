package com.spotify.wrapped.service;

import com.spotify.wrapped.document.PlaybackHistoryDocument;
import com.spotify.wrapped.model.PlayHistoryItem;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaybackSyncService {

    private final SpotifyClientService spotifyClientService;
    private final PlaybackHistoryRepository playbackHistoryRepository;

    public PlaybackSyncService(SpotifyClientService spotifyClientService, PlaybackHistoryRepository playbackHistoryRepository) {
        this.spotifyClientService = spotifyClientService;
        this.playbackHistoryRepository = playbackHistoryRepository;
    }

    public int syncRecentPlaybacks(String accessToken, String spotifyId) {

        List<PlayHistoryItem> recentItems = spotifyClientService.getRecentlyPlayed(accessToken);

        if (recentItems == null || recentItems.isEmpty()) {
            return 0;
        }

        List<PlaybackHistoryDocument> newRecords = new ArrayList<>();

        for (PlayHistoryItem item : recentItems) {

            if (!playbackHistoryRepository.existsBySpotifyIdAndPlayedAt(spotifyId, item.playedAt())) {
                PlaybackHistoryDocument newDocument = new PlaybackHistoryDocument(
                        spotifyId,
                        item.playedAt(),
                        item.track()
                );
                newRecords.add(newDocument);
            }
        }

        // save all at once - batch save
        if (!newRecords.isEmpty()) {
            playbackHistoryRepository.saveAll(newRecords);
        }

        return newRecords.size();
    }
}