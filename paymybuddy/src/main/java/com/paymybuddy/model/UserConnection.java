package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a friendship connection
 * between two users.
 */
@Entity
@Table(name = "users_connections")
@Getter
@Setter
public class UserConnection {

    /**
     * User owning the connection.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Friend linked to the user.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;
}