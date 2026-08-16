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

    /**
     * Synchronizuje historię odtworzeń. Pobiera ostatnie 50 utworów i zapisuje tylko nowości.
     * Zwraca liczbę nowo dodanych utworów (np. do celów logowania w konsoli).
     */
    public int syncRecentPlaybacks(String accessToken, String spotifyId) {
        // 1. Pobieramy z API Spotify to, co user ostatnio słuchał (max 50)
        List<PlayHistoryItem> recentItems = spotifyClientService.getRecentlyPlayed(accessToken);

        if (recentItems == null || recentItems.isEmpty()) {
            return 0; // Brak danych do synchronizacji
        }

        // Przygotowujemy "koszyk" na utwory, których jeszcze nie mamy w bazie
        List<PlaybackHistoryDocument> newRecords = new ArrayList<>();

        // 2. Iterujemy przez to, co przyszło ze Spotify
        for (PlayHistoryItem item : recentItems) {

            // MAGIA: Pytamy naszą szybką bazę przez indeks (zwraca tylko boolean)
            boolean alreadyExists = playbackHistoryRepository.existsBySpotifyIdAndPlayedAt(
                    spotifyId,
                    item.playedAt()
            );

            if (!alreadyExists) {
                // Skoro tego nie było, tworzymy dokument dla MongoDB
                PlaybackHistoryDocument newDocument = new PlaybackHistoryDocument(
                        spotifyId,
                        item.playedAt(),
                        item.track()
                );
                // Wrzucamy do koszyka
                newRecords.add(newDocument);
            }
        }

        // 3. Zapisujemy cały koszyk za jednym zamachem (saveAll jest dużo szybsze niż zapis w pętli!)
        if (!newRecords.isEmpty()) {
            playbackHistoryRepository.saveAll(newRecords);
        }

        // Zwracamy ile nowych dodaliśmy
        return newRecords.size();
    }
}