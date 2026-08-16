package com.spotify.wrapped.controller;

import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PrivacyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Prefix dla wszystkich ścieżek w tej klasie
public class AdminController {

    private final UserRepository userRepository;
    private final PrivacyService privacyService; // Nasz nowy serwis

    public AdminController(UserRepository userRepository, PrivacyService privacyService) {
        this.userRepository = userRepository;
        this.privacyService = privacyService;
    }

    // Zwraca widok panelu
    @GetMapping
    public String getAdminPanel(Model model) {
        // Lista wszystkich użytkowników z bazy SQL
        model.addAttribute("users", userRepository.findAll());

        // Lista Oczekujących Próśb (PENDING) z bazy SQL
        model.addAttribute("pendingRequests", privacyService.getPendingRequests());

        return "admin";
    }

    // Endpoint do akceptacji prośby
    @PostMapping("/request/{id}/approve")
    public String approveRequest(@PathVariable Long id) {
        privacyService.approveRequest(id);
        return "redirect:/admin"; // Odśwież stronę po kliknięciu
    }

    // Endpoint do odrzucenia prośby
    @PostMapping("/request/{id}/reject")
    public String rejectRequest(@PathVariable Long id) {
        privacyService.rejectRequest(id);
        return "redirect:/admin"; // Odśwież stronę po kliknięciu
    }
}