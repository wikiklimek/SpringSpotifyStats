package com.spotify.wrapped.service;

import com.spotify.wrapped.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SpotifyClientService {

    private final RestClient restClient;

    public SpotifyClientService() {
        // Konfigurujemy klienta bazowego raz dla wszystkich zapytań
        this.restClient = RestClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                .build();
    }

    // 1. Pobieranie danych profilu
    public UserProfile getUserProfile(String accessToken) {
        return restClient.get()
                .uri("/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(UserProfile.class); // Magia - Spring sam tłumaczy JSON na UserProfile!
    }

    // 2. Pobieranie Top Tracks (z limitem)
    public List<Track> getTopTracks(String accessToken, int limit) {
        var response = restClient.get()
                .uri("/me/top/tracks?limit={limit}", limit)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<SpotifyPage<Track>>() {});
        // Używamy TypeReference, by powiedzieć Springowi o typie generycznym

        return response != null ? response.items() : List.of();
    }

    // 3. Pobieranie Top Artists (z limitem)
    public List<Artist> getTopArtists(String accessToken, int limit) {
        var response = restClient.get()
                .uri("/me/top/artists?limit={limit}", limit)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<SpotifyPage<Artist>>() {});

        return response != null ? response.items() : List.of();
    }

    // 4. Pobieranie ostatnio odtwarzanych (Recently Played)
    // UWAGA: Endpoint recently-played zwraca nieco inną strukturę (PlayHistoryObject),
    // Zróbmy więc pomocniczy rekord w locie lub osobny DTO
    public List<Track> getRecentlyPlayedTracks(String accessToken, int limit) {
        // Zgodnie z Twoim starym kodem, API zwraca tu listę obiektów, które w środku mają 'track'
        // Skonfigurujemy to precyzyjnie przy refaktoryzacji statystyk
        return List.of();
    }

    // Pobiera to, co aktualnie gra
    public CurrentlyPlayingResponse getCurrentlyPlaying(String token) {
        return restClient.get()
                .uri("https://api.spotify.com/v1/me/player/currently-playing")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                // Jeśli nic nie gra, Spotify zwraca kod 204 (No Content), co mogłoby rzucić błędem.
                // Używamy .body(), ale jeśli jest pusto, łapiemy to i zwracamy null
                .body(CurrentlyPlayingResponse.class);
    }

    // Pobiera ostatnio odtwarzane (max 50 sztuk, bo taki jest limit API)
    public List<PlayHistoryItem> getRecentlyPlayed(String token) {
        RecentlyPlayedResponse response = restClient.get()
                .uri("https://api.spotify.com/v1/me/player/recently-played?limit=50")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(RecentlyPlayedResponse.class);

        return response != null ? response.items() : List.of();
    }
}