package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;


public record PlayHistoryItem(
        Track track,
        @JsonProperty("played_at") Instant playedAt // replacing string
) {}