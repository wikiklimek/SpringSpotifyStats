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

    // autimatically formularz logowania Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AdminEntity adminEntity = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));

        //database entity to sprint security object
        return new User(
                adminEntity.getUsername(),
                adminEntity.getPassword(), // Tutaj wpada nasze {noop}admin123
                Collections.singletonList(new SimpleGrantedAuthority(adminEntity.getRole()))
        );
    }
}