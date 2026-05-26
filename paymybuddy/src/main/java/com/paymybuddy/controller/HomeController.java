package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling home page routes.
 */
@Controller
public class HomeController {

    /**
     * Displays home page.
     *
     * @return home page template
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}