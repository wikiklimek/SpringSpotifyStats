package com.spotify.wrapped.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deletion_requests")
public class DeletionRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String spotifyId; // Kto prosi o usunięcie

    @Column(nullable = false)
    private int daysToKeep; // Ile dni chce zostawić (np. 7, 30, 90)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status; // Nasz nowy Enum (zapisze się w bazie jako tekst)

    @Column(nullable = false)
    private LocalDateTime requestDate; // Kiedy złożono prośbę

    public DeletionRequestEntity() {}

    public DeletionRequestEntity(String spotifyId, int daysToKeep) {
        this.spotifyId = spotifyId;
        this.daysToKeep = daysToKeep;
        this.status = RequestStatus.PENDING; // Domyślnie nowa prośba czeka na akceptację
        this.requestDate = LocalDateTime.now();
    }

    // --- Gettery i Settery ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public int getDaysToKeep() { return daysToKeep; }
    public void setDaysToKeep(int daysToKeep) { this.daysToKeep = daysToKeep; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
}