package com.paymybuddy.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for UserConnection entity.
 */
public class UserConnectionId implements Serializable {

    private Long user;
    private Long friend;

    /**
     * Default constructor.
     */
    public UserConnectionId() {
    }

    /**
     * Constructor with fields.
     *
     * @param user user id
     * @param friend friend id
     */
    public UserConnectionId(Long user, Long friend) {
        this.user = user;
        this.friend = friend;
    }

    /**
     * Equals method for composite key comparison.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConnectionId)) return false;
        UserConnectionId that = (UserConnectionId) o;
        return Objects.equals(user, that.user)
                && Objects.equals(friend, that.friend);
    }

    /**
     * Hashcode method for composite key.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}