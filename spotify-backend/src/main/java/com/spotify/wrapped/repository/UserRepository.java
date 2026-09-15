package com.spotify.wrapped.repository;

import com.spotify.wrapped.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // (SELECT * FROM users WHERE spotify_id = spotifyId)
    Optional<UserEntity> findBySpotifyId(String spotifyId);
}