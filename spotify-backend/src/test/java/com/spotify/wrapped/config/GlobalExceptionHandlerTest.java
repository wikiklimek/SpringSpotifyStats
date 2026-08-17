package com.spotify.wrapped.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleSpotifyTokenExpired() {
        // GIVEN
        ClientAuthorizationRequiredException ex = new ClientAuthorizationRequiredException("spotify");

        // WHEN
        ResponseEntity<?> response = handler.handleSpotifyTokenExpired(ex);

        // THEN
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("musisz się zalogować ponownie", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void shouldHandleIllegalArgument() {
        // GIVEN
        IllegalArgumentException ex = new IllegalArgumentException("Zły parametr!");

        // WHEN
        ResponseEntity<?> response = handler.handleIllegalArgument(ex);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Zły parametr!", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void shouldHandleAllOtherExceptions() {
        // GIVEN
        Exception ex = new RuntimeException("Baza danych płonie!");

        // WHEN
        ResponseEntity<?> response = handler.handleAllExceptions(ex);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("błąd bazy danych lub serwera", ((Map<?, ?>) response.getBody()).get("error"));
    }
}