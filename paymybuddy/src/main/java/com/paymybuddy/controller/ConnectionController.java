package com.paymybuddy.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserConnectionService;
import com.paymybuddy.service.UserService;

/**
 * Controller handling user connections.
 */
@Controller
public class ConnectionController {

    private final UserService userService;
    private final UserConnectionService userConnectionService;

    /**
     * Constructor injection.
     *
     * @param userService service used for user operations
     * @param userConnectionService service used for user connections
     */
    public ConnectionController(
            UserService userService,
            UserConnectionService userConnectionService) {

        this.userService = userService;
        this.userConnectionService = userConnectionService;
    }

    /**
     * Displays add connection page.
     *
     * @param model model used to send user data
     * @param principal connected user
     * @return connection page
     */
    @GetMapping("/connection")
    public String showConnectionPage(Model model, Principal principal) {

        User connectedUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("user", connectedUser);

        return "connection";
    }

    /**
     * Adds a new connection for the connected user.
     *
     * @param email email of the user to add
     * @param principal connected user
     * @return redirect to transfer page
     */
    @PostMapping("/connection")
    public String addConnection(
            @RequestParam String email,
            Principal principal) {

        User connectedUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Connected user not found"));

        User friend = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User to add not found"));

        userConnectionService.addConnection(connectedUser, friend);

        return "redirect:/transfer";
    }
}