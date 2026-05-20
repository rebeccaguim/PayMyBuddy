package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a money transfer transaction
 * between two users.
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    /**
     * Unique transaction identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User sending the money.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * User receiving the money.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    /**
     * Transaction description.
     */
    @Column(length = 255)
    private String description;

    /**
     * Transaction amount.
     */
    @Column(nullable = false)
    private Double amount;
}