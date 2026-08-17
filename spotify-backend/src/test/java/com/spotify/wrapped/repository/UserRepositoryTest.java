package com.spotify.wrapped.repository;

import com.spotify.wrapped.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserBySpotifyId() {
        // GIVEN: Unikalne ID tylko dla tego testu
        UserEntity user = new UserEntity("test_jpa_999", "Wiktoria", "test@test.com", LocalDate.now());
        userRepository.save(user);

        // WHEN
        Optional<UserEntity> foundUser = userRepository.findBySpotifyId("test_jpa_999");

        // THEN
        assertTrue(foundUser.isPresent(), "Użytkownik powinien zostać znaleziony w bazie");
        assertEquals("Wiktoria", foundUser.get().getDisplayName());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }
}