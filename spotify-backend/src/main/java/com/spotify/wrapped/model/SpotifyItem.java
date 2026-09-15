package com.spotify.wrapped.model;

import java.util.List;

public interface SpotifyItem {

    List<Image> images();

    ExternalUrls externalUrls();

    default String getFirstImageUrl(String defaultImageUrl) {
        return (images() != null && !images().isEmpty()) ? images().get(0).url() : defaultImageUrl;
    }
}