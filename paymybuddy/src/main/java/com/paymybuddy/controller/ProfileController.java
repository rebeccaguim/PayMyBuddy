package com.paymybuddy.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;

/**
 * Controller handling profile page.
 */
@Controller
public class ProfileController {

    private final UserService userService;

    /**
     * Constructor injection.
     *
     * @param userService service used for user operations
     */
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays profile page.
     */
    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {

        User connectedUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("user", connectedUser);

        return "profile";
    }

    /**
     * Updates user profile.
     */
    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            Principal principal) {

        User connectedUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userService.updateProfile(connectedUser, username, email, password);

        return "redirect:/profile";
    }
}