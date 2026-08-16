package com.spotify.wrapped.model;

import java.util.List;

public record RecentlyPlayedResponse(
        List<PlayHistoryItem> items
) {}