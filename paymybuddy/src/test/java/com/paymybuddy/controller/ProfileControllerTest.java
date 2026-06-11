package com.paymybuddy.controller;

import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;

/**
 * Unit tests for ProfileController.
 */
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private ProfileController profileController;

    private User user;

    @BeforeEach
    void setUp() {
        profileController = new ProfileController(userService);

        user = new User();
        user.setId(1L);
        user.setUsername("Rebecca");
        user.setEmail("rebecca@test.com");
        user.setPassword("password");
        user.setBalance(100.0);
    }

    @Test
    void showProfilePageShouldReturnProfileView() {

        when(principal.getName()).thenReturn("rebecca@test.com");
        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(user));

        String viewName =
                profileController.showProfilePage(model, principal);

        verify(model).addAttribute("user", user);

        org.junit.jupiter.api.Assertions.assertEquals(
                "profile",
                viewName
        );
    }

    @Test
    void updateProfileShouldRedirectToProfilePage() {

        when(principal.getName()).thenReturn("rebecca@test.com");
        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(user));

        String viewName =
                profileController.updateProfile(
                        "Rebecca Updated",
                        "updated@test.com",
                        "newPassword",
                        principal
                );

        verify(userService)
                .updateProfile(
                        user,
                        "Rebecca Updated",
                        "updated@test.com",
                        "newPassword"
                );

        org.junit.jupiter.api.Assertions.assertEquals(
                "redirect:/profile",
                viewName
        );
    }

    @Test
    void updateProfileShouldWorkWithoutPassword() {

        when(principal.getName()).thenReturn("rebecca@test.com");
        when(userService.getUserByEmail(anyString()))
                .thenReturn(Optional.of(user));

        String viewName =
                profileController.updateProfile(
                        "Rebecca Updated",
                        "updated@test.com",
                        "",
                        principal
                );

        verify(userService)
                .updateProfile(
                        user,
                        "Rebecca Updated",
                        "updated@test.com",
                        ""
                );

        org.junit.jupiter.api.Assertions.assertEquals(
                "redirect:/profile",
                viewName
        );
    }
}