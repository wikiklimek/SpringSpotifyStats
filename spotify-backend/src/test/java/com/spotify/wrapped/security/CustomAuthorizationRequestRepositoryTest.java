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

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId("test-client")
                .authorizationUri("https://spotify.com")
                .state("KROTKI")
                .build();

        // WHEN:
        repository.saveAuthorizationRequest(authRequest, request, response);

        // THEN:
        assertNotNull(request.getSession());
    }
}