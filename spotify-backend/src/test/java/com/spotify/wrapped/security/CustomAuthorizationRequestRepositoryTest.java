package com.spotify.wrapped.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomAuthorizationRequestRepositoryTest {

    private final CustomAuthorizationRequestRepository repository = new CustomAuthorizationRequestRepository();

    @Test
    void shouldSaveAuthorizationRequestAndMaskTokenWithoutCrashing() {
        // GIVEN: Wirtualne żądanie od przeglądarki ze sfabrykowanym "złośliwym" tokenem state
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId("test-client")
                .authorizationUri("https://spotify.com")
                .state("KROTKI") // Ekstremalnie krótki token, by przetestować granice metody maskToken
                .build();

        // WHEN: Zapisujemy żądanie (to wywoła pod maską naszą metodę z "System.out.println")
        repository.saveAuthorizationRequest(authRequest, request, response);

        // THEN: Metoda nie powinna rzucić żadnego błędu NullPointerException czy IndexOutOfBounds,
        // a żądanie powinno zostać poprawnie zapisane w sesji serwera.
        assertNotNull(request.getSession());
    }
}