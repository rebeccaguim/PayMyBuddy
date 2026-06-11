package com.paymybuddy.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserConnectionService;
import com.paymybuddy.service.UserService;

/**
 * Unit tests for TransferController.
 */
@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConnectionService userConnectionService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private TransferController transferController;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {

        transferController = new TransferController(
                userService,
                userConnectionService,
                transactionService);

        sender = new User();
        sender.setId(1L);
        sender.setUsername("Rebecca");
        sender.setEmail("rebecca@test.com");
        sender.setBalance(100.0);

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Sarah");
        receiver.setEmail("sarah@test.com");
        receiver.setBalance(50.0);
    }

    @Test
    void showTransferPageShouldReturnTransferView() {

        when(principal.getName()).thenReturn("rebecca@test.com");

        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(sender));

        when(userConnectionService.getConnectionsByUser(sender))
                .thenReturn(List.of());

        when(transactionService.getTransactionsBySender(sender))
                .thenReturn(List.of());

        String viewName =
                transferController.showTransferPage(
                        model,
                        principal,
                        null,
                        null);

        assertEquals("transfer", viewName);

        verify(model).addAttribute("user", sender);
        verify(model).addAttribute("connections", List.of());
        verify(model).addAttribute("transactions", List.of());
    }

    @Test
    void createTransferShouldRedirectWithSuccessMessage() {

        when(principal.getName()).thenReturn("rebecca@test.com");

        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(sender));

        when(userService.getUserById(2L))
                .thenReturn(Optional.of(receiver));

        String viewName =
                transferController.createTransfer(
                        2L,
                        "Restaurant",
                        20.0,
                        principal);

        assertEquals(
                "redirect:/transfer?success=ok",
                viewName);

        verify(transactionService)
                .transferMoney(
                        sender,
                        receiver,
                        20.0,
                        "Restaurant");
    }

    @Test
    void createTransferShouldRedirectWithErrorMessage() {

        when(principal.getName()).thenReturn("rebecca@test.com");

        when(userService.getUserByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(sender));

        when(userService.getUserById(2L))
                .thenReturn(Optional.of(receiver));

        doThrow(new IllegalArgumentException("Insufficient balance"))
                .when(transactionService)
                .transferMoney(
                        sender,
                        receiver,
                        200.0,
                        "Restaurant");

        String viewName =
                transferController.createTransfer(
                        2L,
                        "Restaurant",
                        200.0,
                        principal);

        assertEquals(
                "redirect:/transfer?error=ko",
                viewName);
    }
}