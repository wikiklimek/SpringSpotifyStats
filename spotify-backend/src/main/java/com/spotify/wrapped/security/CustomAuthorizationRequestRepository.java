package com.spotify.wrapped.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final HttpSessionOAuth2AuthorizationRequestRepository delegate = new HttpSessionOAuth2AuthorizationRequestRepository();

    private String maskToken(String token) {
        if (token == null || token.length() <= 6) {
            return "***";
        }
        return token.substring(0, 3) + "..." + token.substring(token.length() - 3);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest != null) {
            System.out.println("LOG [OAUTH2 ETAP 1.5]: Zapisuję żądanie do schowka w RAM-ie przed wylotem do Spotify...");
            // Używamy metody maskującej!
            System.out.println("  -> Zabezpieczenie CSRF (state): " + maskToken(authorizationRequest.getState()));
        }
        delegate.saveAuthorizationRequest(authorizationRequest, request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = delegate.removeAuthorizationRequest(request, response);

        if (authRequest != null) {
            System.out.println("\n=== [OAUTH2 ETAP 1.5] ===");
            System.out.println("LOG: Użytkownik wrócił ze Spotify na nasz adres powrotny!");
            System.out.println("  -> Wyciągam z pamięci RAM zabezpieczenie CSRF do weryfikacji...");

            String codeFromSpotify = request.getParameter("code");
            String stateFromSpotify = request.getParameter("state");

            if (codeFromSpotify != null) {
                System.out.println("  -> Przechwycono kod jednorazowy od Spotify: [UKRYTY]");
                // Używamy metody maskującej!
                System.out.println("  -> Przechwycono state od Spotify: " + maskToken(stateFromSpotify));

                if (stateFromSpotify.equals(authRequest.getState())) {
                    System.out.println("  -> WERYFIKACJA CSRF ZAKOŃCZONA SUKCESEM! Kod jest bezpieczny.");
                } else {
                    System.out.println("  -> ALARM! Parametr state się nie zgadza! Możliwy atak!");
                }
            }
        }
        return authRequest;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return delegate.loadAuthorizationRequest(request);
    }
}