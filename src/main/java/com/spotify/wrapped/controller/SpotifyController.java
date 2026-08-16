package com.spotify.wrapped.controller;

import com.spotify.wrapped.document.DailyTopArtistsDocument;
import com.spotify.wrapped.document.DailyTopTracksDocument;
import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.model.Artist;
import com.spotify.wrapped.model.CurrentlyPlayingResponse;
import com.spotify.wrapped.model.Track;
import com.spotify.wrapped.repository.DailyTopArtistsRepository;
import com.spotify.wrapped.repository.DailyTopTracksRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PlaybackSyncService;
import com.spotify.wrapped.service.SpotifyClientService;
import com.spotify.wrapped.service.SpotifyStatsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SpotifyController {

    private final SpotifyClientService spotifyService;
    private final UserRepository userRepository;
    private final DailyTopTracksRepository topTracksRepository;
    private final DailyTopArtistsRepository topArtistsRepository;
    private final SpotifyStatsService statsService;
    private final PlaybackSyncService playbackSyncService;

    public SpotifyController(SpotifyClientService spotifyService,
                             UserRepository userRepository,
                             DailyTopTracksRepository topTracksRepository,
                             DailyTopArtistsRepository topArtistsRepository,
                             SpotifyStatsService statsService,
                             PlaybackSyncService playbackSyncService) {
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
        this.topTracksRepository = topTracksRepository;
        this.topArtistsRepository = topArtistsRepository;
        this.statsService = statsService;
        this.playbackSyncService = playbackSyncService;
    }

    // 1. Zwykłe wejście - tylko zapisuje usera i wyświetla bazowy szablon/profil
    @GetMapping("/user-info")
    public String getUserInfo(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient, Model model) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        var userProfile = spotifyService.getUserProfile(token);

        Optional<UserEntity> existingUser = userRepository.findBySpotifyId(userProfile.id());
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setLastLoginDate(LocalDate.now());
            userRepository.save(user);
        } else {
            userRepository.save(new UserEntity(userProfile.id(), userProfile.displayName(), userProfile.email(), LocalDate.now()));
        }

        model.addAttribute("user", userProfile);
        return "user-info"; // Tu na razie możesz mieć samo powitanie w HTML
    }

    // 2. Endpoint tylko dla piosenek (Zwraca JSON)
    @GetMapping("/api/top-tracks")
    @ResponseBody
    public List<Track> getTopTracks(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = spotifyService.getUserProfile(token).id();
        LocalDate today = LocalDate.now();

        return topTracksRepository.findBySpotifyIdAndDate(spotifyId, today)
                .map(DailyTopTracksDocument::getTopTracks)
                .orElseGet(() -> {
                    List<Track> tracks = spotifyService.getTopTracks(token, 10);
                    topTracksRepository.save(new DailyTopTracksDocument(spotifyId, today, tracks));
                    return tracks;
                });
    }

    // 3. Endpoint tylko dla artystów (Zwraca JSON)
    @GetMapping("/api/top-artists")
    @ResponseBody
    public List<Artist> getTopArtists(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = spotifyService.getUserProfile(token).id();
        LocalDate today = LocalDate.now();

        return topArtistsRepository.findBySpotifyIdAndDate(spotifyId, today)
                .map(DailyTopArtistsDocument::getTopArtists)
                .orElseGet(() -> {
                    List<Artist> artists = spotifyService.getTopArtists(token, 10);
                    topArtistsRepository.save(new DailyTopArtistsDocument(spotifyId, today, artists));
                    return artists;
                });
    }

    // 4. Endpoint dla Gatunków (oblicza na bieżąco na podstawie artystów) (Zwraca JSON)
    @GetMapping("/api/top-genres")
    @ResponseBody
    public Map<String, Long> getTopGenres(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        // Najpierw wywołujemy naszą metodę wyżej, żeby upewnić się, że mamy artystów (z DB lub z API)
        List<Artist> topArtists = getTopArtists(authorizedClient);
        return statsService.calculateTopGenres(topArtists, 5);
    }

    // 5. Endpoint Synchronizujący historię (Recently Played) (Zwraca JSON informujący o statusie)
    @GetMapping("/api/recently-played/sync")
    @ResponseBody
    public String syncRecentlyPlayed(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        String spotifyId = spotifyService.getUserProfile(token).id();

        int added = playbackSyncService.syncRecentPlaybacks(token, spotifyId);
        return "Zsynchronizowano pomyślnie. Nowe utwory: " + added;
    }

    // 6. Endpoint dla Aktualnie Odtwarzanego Utworu (Currently Playing) (Zwraca JSON)
    @GetMapping("/api/currently-playing")
    @ResponseBody
    public CurrentlyPlayingResponse getCurrentlyPlaying(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        String token = authorizedClient.getAccessToken().getTokenValue();
        return spotifyService.getCurrentlyPlaying(token);
    }
}