package com.spotify.wrapped.config;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void shouldCreateAdminUserIfItDoesNotExist() throws Exception {
        // GIVEN: W bazie nie ma admina
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // WHEN
        dataInitializer.run();

        // THEN: Zapisujemy nowego admina!
        verify(adminRepository, times(1)).save(any(AdminEntity.class));
    }

    @Test
    void shouldNotCreateAdminUserIfItAlreadyExists() throws Exception {
        // GIVEN: W bazie już jest admin
        AdminEntity existingAdmin = new AdminEntity("admin", "tajne", "ROLE_ADMIN");
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));

        // WHEN
        dataInitializer.run();

        // THEN: Nic nie zapisujemy!
        verify(adminRepository, never()).save(any(AdminEntity.class));
    }
}