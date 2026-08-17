package com.spotify.wrapped.service;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Magia Mockito!
class CustomUserDetailsServiceTest {

    @Mock
    private AdminRepository adminRepository; // Udawana baza danych

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService; // Testowany serwis

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // GIVEN
        AdminEntity admin = new AdminEntity("admin", "{noop}haslo123", "ROLE_ADMIN");
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        // WHEN
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        // THEN
        assertEquals("admin", userDetails.getUsername());
        assertEquals("{noop}haslo123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(adminRepository, times(1)).findByUsername("admin");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // GIVEN
        when(adminRepository.findByUsername("haker")).thenReturn(Optional.empty());

        // WHEN & THEN
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("haker");
        });

        assertEquals("Nie znaleziono użytkownika: haker", exception.getMessage());
    }
}