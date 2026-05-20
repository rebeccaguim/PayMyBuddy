package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing an application user.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username chosen by the user.
     */
    @Column(nullable = false, length = 50)
    private String username;

    /**
     * User email address.
     * Must be unique.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Encrypted user password.
     */
    @Column(nullable = false, length = 250)
    private String password;

    /**
     * Current user account balance.
     */
    @Column(nullable = false)
    private Double balance;
}