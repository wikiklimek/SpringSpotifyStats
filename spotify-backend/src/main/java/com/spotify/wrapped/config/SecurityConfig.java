package com.spotify.wrapped.config;

import com.spotify.wrapped.security.CustomAccessTokenResponseClient;
import com.spotify.wrapped.security.CustomAuthorizationRequestResolver;
import com.spotify.wrapped.security.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.spotify.wrapped.security.CustomAuthorizationRequestRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // DI
    private final CustomAuthorizationRequestResolver customAuthorizationResolver;
    private final CustomAccessTokenResponseClient customTokenClient;
    private final CustomOAuth2UserService customUserService;
    private final CustomAuthorizationRequestRepository customRequestRepository;

    public SecurityConfig(CustomAuthorizationRequestResolver customAuthorizationResolver,
                          CustomAccessTokenResponseClient customTokenClient,
                          CustomOAuth2UserService customUserService,
                          CustomAuthorizationRequestRepository customRequestRepository) {
        this.customAuthorizationResolver = customAuthorizationResolver;
        this.customTokenClient = customTokenClient;
        this.customUserService = customUserService;
        this.customRequestRepository = customRequestRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                request -> request.getServletPath().startsWith("/api/")
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/images/**").permitAll()
                        .requestMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .authorizationRequestResolver(customAuthorizationResolver)
                                .authorizationRequestRepository(customRequestRepository)
                        )
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenResponseClient(customTokenClient)
                        )
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customUserService)
                        )
                        .defaultSuccessUrl("http://127.0.0.1:5173/user", true)
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("http://127.0.0.1:5173/admin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("http://127.0.0.1:5173")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}