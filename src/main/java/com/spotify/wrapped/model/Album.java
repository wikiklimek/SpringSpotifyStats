package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Album(
        String id,
        String name,
        @JsonProperty("album_type") String albumType,
        @JsonProperty("total_tracks") int totalTracks,
        List<Image> images,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("external_urls") ExternalUrls externalUrls
) implements SpotifyItem {}
