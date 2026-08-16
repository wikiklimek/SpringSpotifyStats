package com.spotify.wrapped.entity;

public enum RequestStatus {
    PENDING,   // Oczekuje na decyzję Admina
    APPROVED,  // Zaakceptowana i wykonana
    REJECTED,  // Odrzucona przez Admina
    WITHDRAWN  // Wycofana przez samego użytkownika
}