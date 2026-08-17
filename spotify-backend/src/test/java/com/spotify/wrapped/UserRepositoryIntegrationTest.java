package com.spotify.wrapped;

import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional; // NOWY IMPORT

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional // MAGIA: Po wykonaniu testu, Spring usunie dodanego użytkownika!
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        // GIVEN: Zmienione ID na unikalne dla tego testu
        UserEntity user = new UserEntity("test_integration_123", "Wiktoria", "test@test.com", LocalDate.now());
        userRepository.save(user);

        // WHEN
        Optional<UserEntity> foundUser = userRepository.findBySpotifyId("test_integration_123");

        // THEN
        assertTrue(foundUser.isPresent());
        assertEquals("Wiktoria", foundUser.get().getDisplayName());
    }
}