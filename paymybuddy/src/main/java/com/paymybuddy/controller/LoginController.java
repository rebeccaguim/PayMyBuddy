package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling login page routes.
 */
@Controller
public class LoginController {

    /**
     * Displays the login page.
     *
     * @return login page template
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}