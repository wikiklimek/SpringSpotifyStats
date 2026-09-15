package com.spotify.wrapped.config;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        // mocking values that are normally injected with  @Value by Spring
        ReflectionTestUtils.setField(dataInitializer, "defaultUsername", "admin");
        ReflectionTestUtils.setField(dataInitializer, "defaultPassword", "someSwecretAdmina2026!");
    }

    @Test
    void shouldCreateAdminUserIfItDoesNotExist() throws Exception {
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$zaszyfrowanyHashBCrypt");

        dataInitializer.run();

        verify(passwordEncoder, times(1)).encode("someSwecretAdmina2026!");
        verify(adminRepository, times(1)).save(any(AdminEntity.class));
    }

    @Test
    void shouldNotCreateAdminUserIfItAlreadyExists() throws Exception {
        AdminEntity existingAdmin = new AdminEntity("admin", "$2a$10$staryHash", "ROLE_ADMIN");
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));

        dataInitializer.run();

        verify(passwordEncoder, never()).encode(anyString());
        verify(adminRepository, never()).save(any(AdminEntity.class));
    }
}