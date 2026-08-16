package com.spotify.wrapped.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Ścieżki publiczne (dostępne dla każdego)
                        .requestMatchers("/", "/login", "/css/**", "/images/**").permitAll()

                        // Panel admina wymaga roli "ADMIN" (Spring Security sam obetnie przedrostek "ROLE_")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Wszystkie inne żądania (czyli nasze /user-info) wymagają bycia po prostu zalogowanym
                        .anyRequest().authenticated()
                )
                // Konfiguracja dla kont Spotify (OAuth2) - Spring sam obsługuje tokeny
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/user-info", true)
                )
                // Konfiguracja dla Admina (Tradycyjny formularz logowania)
                .formLogin(form -> form
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                // Konfiguracja wylogowywania
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}