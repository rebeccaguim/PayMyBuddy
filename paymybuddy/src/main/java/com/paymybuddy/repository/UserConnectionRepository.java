package com.paymybuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.model.UserConnectionId;

/**
 * Repository used to access user connection data
 * from the database.
 */
@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, UserConnectionId> {

    /**
     * Finds all connections for a given user.
     *
     * @param user connected user
     * @return list of user connections
     */
    List<UserConnection> findByUser(User user);
}