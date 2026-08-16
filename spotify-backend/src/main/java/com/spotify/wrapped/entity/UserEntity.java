package com.spotify.wrapped.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users") //tak bedzie sie nazywac tabela w bazie
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String spotifyId;

    private String displayName;
    private String email;

    @Column(name = "last_login_date")
    private LocalDate lastLoginDate;

    // Pusty konstruktor wymagany przez Hibernate
    public UserEntity() {}

    // Konstruktor do wygodnego tworzenia
    public UserEntity(String spotifyId, String displayName, String email, LocalDate lastLoginDate) {
        this.spotifyId = spotifyId;
        this.displayName = displayName;
        this.email = email;
        this.lastLoginDate = lastLoginDate;
    }

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}