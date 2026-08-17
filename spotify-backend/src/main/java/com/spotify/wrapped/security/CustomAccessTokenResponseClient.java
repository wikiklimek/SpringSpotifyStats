package com.spotify.wrapped.security;

import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final RestClientAuthorizationCodeTokenResponseClient defaultClient = new RestClientAuthorizationCodeTokenResponseClient();

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest request) {
        System.out.println("\n=== [OAUTH2 ETAP 2] ===");
        System.out.println("LOG: Wysyłam jednorazowy kod z powrotem do Spotify w zamian za Access Token...");

        // Wykonanie ukrytego zapytania
        OAuth2AccessTokenResponse response = defaultClient.getTokenResponse(request);

        System.out.println("  -> SUKCES! Serwer zdobył token (nie wyświetlam go ze względów bezpieczeństwa).");
        System.out.println("  -> Typ tokenu: " + response.getAccessToken().getTokenType().getValue());
        System.out.println("  -> Przyznane zgody: " + response.getAccessToken().getScopes());
        System.out.println("  -> Token wygaśnie: " + response.getAccessToken().getExpiresAt());

        return response;
    }
}