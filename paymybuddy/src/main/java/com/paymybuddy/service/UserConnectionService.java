package com.paymybuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;
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
     * Returns all connections for a given user.
     *
     * @param user connected user
     * @return list of user connections
     */
    public List<UserConnection> getConnectionsByUser(User user) {
        return userConnectionRepository.findByUser(user);
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

    /**
     * Adds a new connection between two users.
     *
     * @param user connected user
     * @param friend user to add as a connection
     */
    public void addConnection(User user, User friend) {

        UserConnection connection = new UserConnection();

        connection.setUser(user);
        connection.setFriend(friend);

        userConnectionRepository.save(connection);
    }
}