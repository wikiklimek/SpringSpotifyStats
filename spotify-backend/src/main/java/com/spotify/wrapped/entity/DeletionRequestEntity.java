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
    private String spotifyId;

    @Column(nullable = false)
    private int daysToKeep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    public DeletionRequestEntity() {}

    public DeletionRequestEntity(String spotifyId, int daysToKeep) {
        this.spotifyId = spotifyId;
        this.daysToKeep = daysToKeep;
        this.status = RequestStatus.PENDING; // by default
        this.requestDate = LocalDateTime.now();
    }

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