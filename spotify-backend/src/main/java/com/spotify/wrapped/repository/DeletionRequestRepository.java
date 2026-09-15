package com.spotify.wrapped.repository;

import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeletionRequestRepository extends JpaRepository<DeletionRequestEntity, Long> {

    Optional<DeletionRequestEntity> findBySpotifyIdAndStatus(String spotifyId, RequestStatus status);

    List<DeletionRequestEntity> findAllByStatusOrderByRequestDateAsc(RequestStatus status);
}