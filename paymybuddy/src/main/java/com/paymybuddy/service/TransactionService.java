package com.paymybuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

/**
 * Service handling transaction business logic.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * Constructor injection.
     *
     * @param transactionRepository repository for transactions
     * @param userRepository repository for users
     */
    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns all transactions.
     *
     * @return list of transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Transfers money between two users.
     * Transaction is automatically rolled back if an error occurs.
     *
     * @param sender sender user
     * @param receiver receiver user
     * @param amount transfer amount
     * @param description transaction description
     */
    @Transactional
    public void transferMoney(User sender,
                              User receiver,
                              Double amount,
                              String description) {

        // Check sender balance
        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Update balances
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // Save updated users
        userRepository.save(sender);
        userRepository.save(receiver);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        // Save transaction
        transactionRepository.save(transaction);
    }
}