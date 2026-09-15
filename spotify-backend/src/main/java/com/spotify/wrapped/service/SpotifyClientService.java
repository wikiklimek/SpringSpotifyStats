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
        this.restClient = RestClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                // listener
                .requestInterceptor((request, body, execution) -> {
                    System.out.println("--> [API SPOTIFY OUT]: Request "
                            + request.getMethod() + " to address: " + request.getURI());
                    long startTime = System.currentTimeMillis();
                    //in spotify servers
                    var response = execution.execute(request, body);
                    long duration = System.currentTimeMillis() - startTime;
                    System.out.println("<-- [API SPOTIFY IN]: code "
                            + response.getStatusCode() + " in " + duration + "ms");
                    return response;
                })
                .build();
    }

    public UserProfile getUserProfile(String accessToken) {
        return restClient.get()
                .uri("/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(UserProfile.class);
    }

    public List<Track> getTopTracks(String accessToken, int limit) {
        var response = restClient.get()
                .uri("/me/top/tracks?limit={limit}", limit)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<SpotifyPage<Track>>() {});
        return response != null ? response.items() : List.of();
    }

    public List<Artist> getTopArtists(String accessToken, int limit) {
        var response = restClient.get()
                .uri("/me/top/artists?limit={limit}", limit)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<SpotifyPage<Artist>>() {});
        return response != null ? response.items() : List.of();
    }

    public CurrentlyPlayingResponse getCurrentlyPlaying(String token) {
        return restClient.get()
                .uri("/me/player/currently-playing")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(CurrentlyPlayingResponse.class);
    }

    public List<PlayHistoryItem> getRecentlyPlayed(String accessToken) {
        RecentlyPlayedResponse response = restClient.get()
                .uri("/me/player/recently-played?limit=50")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(RecentlyPlayedResponse.class);
        return response != null ? response.items() : List.of();
    }
}