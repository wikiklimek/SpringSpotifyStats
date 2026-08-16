package com.spotify.wrapped.config;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;

    public DataInitializer(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Sprawdzamy, czy w bazie istnieje już użytkownik o loginie "admin"
        if (adminRepository.findByUsername("admin").isEmpty()) {

            System.out.println("INFO: Brak konta Administratora. Tworzenie domyślnego konta...");

            // UWAGA: Znacznik {noop} jest tu celowy!
            // Spring Security domyślnie wymaga, aby wszystkie hasła były zaszyfrowane (np. BCrypt).
            // Używając prefiksu {noop}, mówimy Springowi: "Na razie to jest czysty tekst, nie szyfruj tego".
            // Kiedy podepniemy pełne Security, zmienimy to na prawdziwy szyfr.

            AdminEntity admin = new AdminEntity(
                    "admin",
                    "{noop}admin123",
                    "ROLE_ADMIN"
            );

            adminRepository.save(admin);
            System.out.println("INFO: Utworzono konto Administratora (Login: admin, Hasło: admin123)");
        }
    }
}