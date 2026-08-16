package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentlyPlayingResponse(
        @JsonProperty("is_playing") boolean isPlaying,
        Track item // To nasza stara, dobra klasa Track!
) {}