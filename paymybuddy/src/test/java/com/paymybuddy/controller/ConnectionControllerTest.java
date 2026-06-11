package com.paymybuddy.controller;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserConnectionService;
import com.paymybuddy.service.UserService;

/**
 * Unit tests for ConnectionController.
 */
@ExtendWith(MockitoExtension.class)
class ConnectionControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConnectionService userConnectionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private ConnectionController connectionController;

    private User connectedUser;
    private User friend;

    @BeforeEach
    void setUp() {
        connectionController =
                new ConnectionController(userService, userConnectionService);

        connectedUser = new User();
        connectedUser.setId(1L);
        connectedUser.setUsername("Rebecca");
        connectedUser.setEmail("rebecca@test.com");

        friend = new User();
        friend.setId(2L);
        friend.setUsername("Sarah");
        friend.setEmail("sarah@test.com");
    }

    @Test
    void showConnectionPageShouldReturnConnectionView() {
        when(principal.getName()).thenReturn("rebecca@test.com");
        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(connectedUser));

        String viewName = connectionController.showConnectionPage(model, principal);

        assertEquals("connection", viewName);

        verify(model).addAttribute("user", connectedUser);
        verify(userService).getUserByEmail("rebecca@test.com");
    }

    @Test
    void addConnectionShouldAddFriendAndRedirectToTransfer() {
        when(principal.getName()).thenReturn("rebecca@test.com");

        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(connectedUser));

        when(userService.getUserByEmail("sarah@test.com"))
                .thenReturn(Optional.of(friend));

        String viewName =
                connectionController.addConnection("sarah@test.com", principal);

        assertEquals("redirect:/transfer", viewName);

        verify(userConnectionService).addConnection(connectedUser, friend);
    }
}