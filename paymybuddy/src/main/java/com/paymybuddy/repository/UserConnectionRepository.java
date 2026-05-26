package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.UserConnection;
import com.paymybuddy.model.UserConnectionId;

/**
 * Repository used to access user connection data
 * from the database.
 */
@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, UserConnectionId> {

}