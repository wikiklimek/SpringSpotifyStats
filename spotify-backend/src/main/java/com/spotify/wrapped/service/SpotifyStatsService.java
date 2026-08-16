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

    // Zwracamy Mapę, gdzie Klucz to nazwa gatunku, a Wartość to ilość wystąpień
    public Map<String, Long> calculateTopGenres(List<Artist> artists, int limit) {

        if (artists == null || artists.isEmpty()) {
            return Map.of();
        }

        return artists.stream()
                // 1. Wyciągamy listy gatunków z każdego artysty i spłaszczamy je do jednego wielkiego strumienia słów
                .flatMap(artist -> artist.genres().stream())
                // 2. Grupujemy te same słowa i zliczamy ich wystąpienia
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                // 3. Zamieniamy z powrotem na strumień, żeby móc posortować
                .entrySet().stream()
                // 4. Sortujemy malejąco po wartości (ilości wystąpień)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                // 5. Ucinamy do pożądanej ilości (np. Top 10)
                .limit(limit)
                // 6. Zbieramy do LinkedHashMap (LinkedHashMap zachowuje naszą kolejność sortowania!)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}