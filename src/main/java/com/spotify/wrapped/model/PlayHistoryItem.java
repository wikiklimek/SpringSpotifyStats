package com.spotify.wrapped.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Zauważ, że dokumentacja mówi o obiekcie, w którym jest "track" oraz "played_at"
public record PlayHistoryItem(
        Track track,
        @JsonProperty("played_at") String playedAt
) {}