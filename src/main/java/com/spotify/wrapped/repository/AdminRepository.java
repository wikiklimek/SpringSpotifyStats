package com.spotify.wrapped.repository;

import com.spotify.wrapped.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    // Ta metoda przyda nam się za chwilę do logowania Spring Security
    Optional<AdminEntity> findByUsername(String username);
}