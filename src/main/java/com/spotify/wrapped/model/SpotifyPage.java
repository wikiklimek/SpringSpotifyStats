package com.spotify.wrapped.model;

import java.util.List;

// To <T> jest kluczowe! Oznacza "Type" i pozwala nam reużywać tego rekordu dla Artystów, Utworów itp.
public record SpotifyPage<T>(
        List<T> items
) {}