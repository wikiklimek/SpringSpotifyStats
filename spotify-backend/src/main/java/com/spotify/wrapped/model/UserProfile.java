package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UserProfile(
        String id,
        @JsonProperty("display_name") String displayName,
        String email,
        String country,
        List<Image> images,
        @JsonProperty("external_urls") ExternalUrls externalUrls,
        Followers followers,
        String product
) implements SpotifyItem {}