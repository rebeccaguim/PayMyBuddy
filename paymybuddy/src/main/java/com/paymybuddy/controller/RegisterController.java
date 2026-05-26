package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;

/**
 * Controller handling user registration.
 */
@Controller
public class RegisterController {

    private final UserService userService;

    /**
     * Constructor injection for UserService.
     *
     * @param userService service used for user operations
     */
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration page.
     *
     * @param model model used to send an empty user object to the view
     * @return registration page
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Registers a new user.
     *
     * @param user user submitted from the form
     * @return redirect to home page
     */
    @PostMapping("/register")
    public String registerUser(User user) {
        userService.saveUser(user);
        return "redirect:/";
    }
}