package com.spotify.wrapped.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Przechwytuje np. nasze błędy z PrivacyService (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Zwracamy kod 400 (Bad Request) i przyjazny JSON dla frontendu
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    // Fallback - Przechwytuje WSZYSTKIE inne niespodziewane błędy z całej aplikacji
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllOtherExceptions(Exception ex) {
        System.err.println("KRYTYCZNY BŁĄD SYSTEMU: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Wystąpił nieoczekiwany błąd serwera. Spróbuj ponownie później."));
    }
}