package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalUrls(
        @JsonProperty("spotify") String spotifyUrl
) {}