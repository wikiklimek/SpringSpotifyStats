package com.spotify.wrapped.model;

import java.util.List;


public record SpotifyPage<T>(
        List<T> items
) {}