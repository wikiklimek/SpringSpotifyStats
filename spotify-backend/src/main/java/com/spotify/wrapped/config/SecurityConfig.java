package com.spotify.wrapped.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. WŁĄCZAMY CORS (aby React mógł komunikować się ze Springiem)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF wyłączamy (na razie, do testów komunikacji React <-> Spring)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Ścieżki publiczne (dostępne dla każdego)
                        .requestMatchers("/", "/login", "/css/**", "/images/**").permitAll()

                        // Panel admina wymaga roli "ADMIN"
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Wszystkie inne żądania wymagają bycia zalogowanym
                        .anyRequest().authenticated()
                )
                // Konfiguracja dla kont Spotify (OAuth2)
                .oauth2Login(oauth2 -> oauth2
                        // ZMIANA: Po udanym logowaniu wracamy do Reacta!
                        .defaultSuccessUrl("http://localhost:5173", true)
                )
                // Konfiguracja dla Admina
                .formLogin(form -> form
                        // ZMIANA: Po udanym logowaniu wracamy do Reacta!
                        .defaultSuccessUrl("http://localhost:5173", true)
                        .permitAll()
                )
                // Konfiguracja wylogowywania
                .logout(logout -> logout
                        // ZMIANA: Po wylogowaniu wracamy do Reacta!
                        .logoutSuccessUrl("http://localhost:5173")
                        .permitAll()
                );

        return http.build();
    }

    // --- NOWA METODA: KONFIGURACJA CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Zezwalamy na strzały TYLKO z naszego lokalnego Reacta
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Zezwalamy na metody używane w API
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Zezwalamy na wszelkie nagłówki
        configuration.setAllowedHeaders(List.of("*"));

        // KLUCZOWE: Pozwalamy na przesyłanie ciasteczka sesyjnego JSESSIONID między portami 5173 a 8080!
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}