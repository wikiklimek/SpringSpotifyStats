package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Track(
        String id,
        String name,
        int popularity,
        @JsonProperty("duration_ms") int durationMs,
        Album album,
        List<Artist> artists,
        @JsonProperty("external_urls") ExternalUrls externalUrls,
        @JsonProperty("preview_url") String previewUrl,
        boolean explicit
) {}