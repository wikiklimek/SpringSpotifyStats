package com.spotify.wrapped.service;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Ta metoda jest automatycznie wywoływana przez formularz logowania Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Szukamy admina w bazie po loginie
        AdminEntity adminEntity = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));

        // Tłumaczymy naszą bazodanową Encję na obiekt "User", który rozumie Spring Security
        return new User(
                adminEntity.getUsername(),
                adminEntity.getPassword(), // Tutaj wpada nasze {noop}admin123
                Collections.singletonList(new SimpleGrantedAuthority(adminEntity.getRole()))
        );
    }
}