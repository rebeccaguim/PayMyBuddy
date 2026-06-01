package com.paymybuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;

/**
 * Repository used to access transaction data from the database.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds transactions sent by a user.
     *
     * @param sender user who sent transactions
     * @return list of transactions
     */
    List<Transaction> findBySender(User sender);
}