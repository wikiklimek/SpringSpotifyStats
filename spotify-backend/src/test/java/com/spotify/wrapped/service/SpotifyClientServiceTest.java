package com.spotify.wrapped.service;

import com.spotify.wrapped.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotifyClientServiceTest {

    // Tworzymy osobne mocki dla każdego ogniwa w łańcuszku RestClienta!
    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private SpotifyClientService spotifyClientService;

    @Test
    void shouldGetUserProfile() {
        ReflectionTestUtils.setField(spotifyClientService, "restClient", restClient);

        UserProfile mockProfile = new UserProfile("123", "Wika", "w@test.com", "PL", null, null, null, "premium");

        // Ręcznie spinamy łańcuszek: get() -> uri() -> header() -> retrieve() -> body()
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/me")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq("Authorization"), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(UserProfile.class)).thenReturn(mockProfile);

        // WHEN
        UserProfile result = spotifyClientService.getUserProfile("fake-token");

        // THEN
        assertEquals("Wika", result.displayName());
        assertEquals("123", result.id());
        assertEquals("PL", result.country());
    }
}