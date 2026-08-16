package com.spotify.wrapped.model;

import java.util.List;

public interface SpotifyItem {

    // Zmuszamy rekord, żeby miał pole/metodę listującą obrazki
    List<Image> images();

    // Zmuszamy rekord, żeby miał pole/metodę zwracającą URL-e
    ExternalUrls externalUrls();

    // Domyślna logika (default) - piszemy ją raz, a mają ją wszystkie rekordy!
    default String getFirstImageUrl(String defaultImageUrl) {
        return (images() != null && !images().isEmpty()) ? images().get(0).url() : defaultImageUrl;
    }
}