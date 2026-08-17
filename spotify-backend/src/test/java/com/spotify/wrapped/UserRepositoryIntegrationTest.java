package com.spotify.wrapped;

import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // Uruchamia cały kontekst Springa i łączy się z testową bazą!
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        // GIVEN
        UserEntity user = new UserEntity("spotify_123", "Wiktoria", "test@test.com", LocalDate.now());
        userRepository.save(user);

        // WHEN
        Optional<UserEntity> foundUser = userRepository.findBySpotifyId("spotify_123");

        // THEN
        assertTrue(foundUser.isPresent());
        assertEquals("Wiktoria", foundUser.get().getDisplayName());
    }
}