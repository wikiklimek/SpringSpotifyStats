package com.spotify.wrapped.service;

import com.spotify.wrapped.model.Artist;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SpotifyStatsService {

    public Map<String, Long> calculateTopGenres(List<Artist> artists, int limit) {

        if (artists == null || artists.isEmpty()) {
            return Map.of();
        }

        return artists.stream()
                .flatMap(artist -> artist.genres().stream())
                // group by name and count  how many times
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}