package com.spotify.wrapped.controller;

import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UserRepository userRepository;

    // Wstrzykujemy repozytorium PostgreSQL
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model) {

        // Magia Spring Data JPA: findAll() wygeneruje "SELECT * FROM users"
        List<UserEntity> allUsers = userRepository.findAll();

        // Przekazujemy listę do Thymeleafa
        model.addAttribute("users", allUsers);

        return "admin"; // Szukaj pliku admin.html
    }
}