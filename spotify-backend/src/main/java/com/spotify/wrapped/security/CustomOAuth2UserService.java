package com.spotify.wrapped.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("\n=== [OAUTH2 ETAP 3] ===");
        System.out.println("LOG: Otwieram API Spotify, aby sprawdzić tożsamość użytkownika...");

        // data dwonloading feom spotify
        OAuth2User user = delegate.loadUser(userRequest);

        System.out.println("  -> SUKCES! Profil pobrany pomyślnie.");
        System.out.println("  -> Wykryto Użytkownika: " + user.getAttribute("display_name"));
        System.out.println("  -> Spotify ID (Klucz publiczny bazy): " + user.getAttribute("id"));
        System.out.println("  -> Kraj konta: " + user.getAttribute("country"));
        System.out.println("=======================\n");

        return user;
    }
}