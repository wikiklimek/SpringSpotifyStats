package com.spotify.wrapped;

import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        UserEntity user = new UserEntity("test_tc_user", "Wiktoria", "test@test.com", LocalDate.now());
        userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findBySpotifyId("test_tc_user");

        assertTrue(foundUser.isPresent());
        assertEquals("Wiktoria", foundUser.get().getDisplayName());
    }
}