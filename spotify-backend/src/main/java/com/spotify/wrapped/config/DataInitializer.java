package com.spotify.wrapped.config;

import com.spotify.wrapped.entity.AdminEntity;
import com.spotify.wrapped.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-username}")
    private String defaultUsername;

    @Value("${app.admin.default-password}")
    private String defaultPassword;

    public DataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.findByUsername(defaultUsername).isEmpty()) {
            System.out.println("INFO: Brak konta Administratora. Tworzenie bezpiecznego konta...");

            String hashedPassword = passwordEncoder.encode(defaultPassword);

            AdminEntity admin = new AdminEntity(
                    defaultUsername,
                    hashedPassword,
                    "ROLE_ADMIN"
            );

            adminRepository.save(admin);
            System.out.println("INFO: Utworzono konto Administratora (Login: " + defaultUsername + ", Hasło: [ZASZYFROWANE BCRYPT])");
        }
    }
}