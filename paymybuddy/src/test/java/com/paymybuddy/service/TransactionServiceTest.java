package com.paymybuddy.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

/**
 * Unit tests for TransactionService.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    private TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, userRepository);

        sender = new User();
        sender.setId(1L);
        sender.setUsername("Rebecca");
        sender.setEmail("rebecca@test.com");
        sender.setPassword("password");
        sender.setBalance(100.0);

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Sarah");
        receiver.setEmail("sarah@test.com");
        receiver.setPassword("password");
        receiver.setBalance(50.0);
    }

    @Test
    void getAllTransactionsShouldReturnTransactions() {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(20.0);
        transaction.setDescription("Restaurant");

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getAmount());
        verify(transactionRepository).findAll();
    }

    @Test
    void getTransactionsBySenderShouldReturnSenderTransactions() {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(15.0);
        transaction.setDescription("Cinema");

        when(transactionRepository.findBySender(sender)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getTransactionsBySender(sender);

        assertEquals(1, result.size());
        assertEquals("Cinema", result.get(0).getDescription());
        verify(transactionRepository).findBySender(sender);
    }

    @Test
    void transferMoneyShouldUpdateBalancesAndSaveTransaction() {
        transactionService.transferMoney(sender, receiver, 20.0, "Restaurant");

        assertEquals(80.0, sender.getBalance());
        assertEquals(70.0, receiver.getBalance());

        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);

        ArgumentCaptor<Transaction> transactionCaptor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals(sender, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals(20.0, savedTransaction.getAmount());
        assertEquals("Restaurant", savedTransaction.getDescription());
    }

    @Test
    void transferMoneyShouldThrowExceptionWhenBalanceIsInsufficient() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.transferMoney(sender, receiver, 150.0, "Too much")
        );

        assertEquals("Insufficient balance", exception.getMessage());

        verify(userRepository, never()).save(sender);
        verify(userRepository, never()).save(receiver);
        verify(transactionRepository, never()).save(org.mockito.ArgumentMatchers.any(Transaction.class));
    }
}