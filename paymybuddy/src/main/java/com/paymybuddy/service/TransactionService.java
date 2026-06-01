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
     * Returns all transactions sent by a user.
     *
     * @param sender connected user
     * @return list of transactions
     */
    public List<Transaction> getTransactionsBySender(User sender) {
        return transactionRepository.findBySender(sender);
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

        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }
}