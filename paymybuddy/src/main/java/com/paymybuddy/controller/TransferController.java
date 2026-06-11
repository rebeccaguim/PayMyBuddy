package com.paymybuddy.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserConnectionService;
import com.paymybuddy.service.UserService;

/**
 * Controller handling transfer page.
 */
@Controller
public class TransferController {

    private static final Logger logger =
            LoggerFactory.getLogger(TransferController.class);

    private final UserService userService;
    private final UserConnectionService userConnectionService;
    private final TransactionService transactionService;

    public TransferController(
            UserService userService,
            UserConnectionService userConnectionService,
            TransactionService transactionService) {

        this.userService = userService;
        this.userConnectionService = userConnectionService;
        this.transactionService = transactionService;
    }

    @GetMapping("/transfer")
    public String showTransferPage(
            Model model,
            Principal principal,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {

        User connectedUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        logger.info(
                "Transfer page opened by {}",
                connectedUser.getEmail()
        );

        List<UserConnection> connections =
                userConnectionService.getConnectionsByUser(connectedUser);

        List<Transaction> transactions =
                transactionService.getTransactionsBySender(connectedUser);

        model.addAttribute("user", connectedUser);
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions);
        model.addAttribute("error", error);
        model.addAttribute("success", success);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String createTransfer(
            @RequestParam Long receiverId,
            @RequestParam String description,
            @RequestParam Double amount,
            Principal principal) {

        try {
            User sender = userService.getUserByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

            User receiver = userService.getUserById(receiverId)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

            logger.info(
                    "Transfer request: sender={}, receiver={}, amount={}",
                    sender.getEmail(),
                    receiver.getEmail(),
                    amount
            );

            transactionService.transferMoney(sender, receiver, amount, description);

            logger.info(
                    "Transfer successful: sender={}, receiver={}, amount={}",
                    sender.getEmail(),
                    receiver.getEmail(),
                    amount
            );

            return "redirect:/transfer?success=ok";

        } catch (IllegalArgumentException exception) {

            logger.error(
                    "Transfer failed: {}",
                    exception.getMessage()
            );

            return "redirect:/transfer?error=ko";
        }
    }
}