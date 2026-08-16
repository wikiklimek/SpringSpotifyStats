package com.spotify.wrapped.controller;

import com.spotify.wrapped.document.DailyStatsDocument;
import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.model.Artist;
import com.spotify.wrapped.model.Track;
import com.spotify.wrapped.repository.DailyStatsRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.SpotifyClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.spotify.wrapped.service.SpotifyStatsService;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class SpotifyController {

    private final SpotifyClientService spotifyService;
    private final UserRepository userRepository;
    private final DailyStatsRepository dailyStatsRepository; // Wstrzykujemy repo Mongo
    private final SpotifyStatsService statsService;

    public SpotifyController(SpotifyClientService spotifyService,
                             UserRepository userRepository,
                             DailyStatsRepository dailyStatsRepository,
                             SpotifyStatsService statsService) {
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
        this.dailyStatsRepository = dailyStatsRepository;
        this.statsService = statsService;
    }

    @GetMapping("/user-info")
    public String getUserInfo(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient,
            Model model) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        LocalDate today = LocalDate.now();

        // 1. Zawsze pobieramy profil (jest lekki) i zapisujemy/aktualizujemy go w Postgresie
        var userProfile = spotifyService.getUserProfile(token);

        Optional<UserEntity> existingUser = userRepository.findBySpotifyId(userProfile.id());
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setLastLoginDate(today);
            userRepository.save(user);
        } else {
            UserEntity newUser = new UserEntity(userProfile.id(), userProfile.displayName(), userProfile.email(), today);
            userRepository.save(newUser);
        }

        // --- 2. LOGIKA CACHOWANIA (MONGO DB) ---
        List<Track> topTracks;
        List<Artist> topArtists;

        // Pytamy Mongo: "Czy mamy już statystyki tego usera z dzisiaj?"
        Optional<DailyStatsDocument> statsFromToday = dailyStatsRepository.findBySpotifyIdAndDate(userProfile.id(), today);

        if (statsFromToday.isPresent()) {
            // TAK! Mamy dane w naszej bazie! Nie dotykamy limitów Spotify.
            System.out.println("INFO: Pobrano statystyki z naszej bazy MongoDB!");
            DailyStatsDocument stats = statsFromToday.get();
            topTracks = stats.getTopTracks();
            topArtists = stats.getTopArtists();
        } else {
            // NIE! Użytkownik jest tu pierwszy raz dzisiaj. Pytamy Spotify.
            System.out.println("INFO: Pobieram świeże dane ze Spotify i zapisuję do Mongo!");
            topTracks = spotifyService.getTopTracks(token, 10);
            topArtists = spotifyService.getTopArtists(token, 10);

            // Zapisujemy te dane w Mongo, żeby za 5 minut nie pytać o to samo
            DailyStatsDocument newStats = new DailyStatsDocument(userProfile.id(), today, topTracks, topArtists);
            dailyStatsRepository.save(newStats);
        }
        // ---------------------------------------

        // 3. Obliczamy Top 5 gatunków na podstawie naszych artystów
        Map<String, Long> topGenres = statsService.calculateTopGenres(topArtists, 5);

        // 4. Dodajemy wszystko do modelu
        model.addAttribute("user", userProfile);
        model.addAttribute("tracks", topTracks);
        model.addAttribute("artists", topArtists);
        model.addAttribute("genres", topGenres); // Podajemy gatunki do HTML-a

        return "user-info";
    }
}