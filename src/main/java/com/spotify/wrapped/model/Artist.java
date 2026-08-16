package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Artist(
        String id,
        String name,
        int popularity,
        List<String> genres,
        Followers followers,
        List<Image> images, // To implementuje część interfejsu
        @JsonProperty("external_urls") ExternalUrls externalUrls // To implementuje resztę
) implements SpotifyItem {}