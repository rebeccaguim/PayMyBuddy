package com.paymybuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.UserConnectionRepository;

/**
 * Service handling user connection business logic.
 */
@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;

    /**
     * Constructor injection.
     *
     * @param userConnectionRepository repository for user connections
     */
    public UserConnectionService(UserConnectionRepository userConnectionRepository) {

        this.userConnectionRepository = userConnectionRepository;
    }

    /**
     * Returns all user connections.
     *
     * @return list of user connections
     */
    public List<UserConnection> getAllConnections() {
        return userConnectionRepository.findAll();
    }

    /**
     * Saves a user connection.
     *
     * @param connection connection to save
     * @return saved connection
     */
    public UserConnection saveConnection(UserConnection connection) {
        return userConnectionRepository.save(connection);
    }
}