package com.spotify.wrapped.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;


    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                repo, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
        logSafeAuthData(authRequest);
        return authRequest;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
        logSafeAuthData(authRequest);
        return authRequest;
    }

    private void logSafeAuthData(OAuth2AuthorizationRequest authRequest) {
        if (authRequest != null) {
            System.out.println("\n=== [OAUTH2 ETAP 1] ===");
            System.out.println("LOG: Generuję link do panelu logowania Spotify...");
            System.out.println("  -> Żądane zgody (scopes): " + authRequest.getScopes());
            System.out.println("  -> Adres powrotny (redirect_uri): " + authRequest.getRedirectUri());
        }
    }
}