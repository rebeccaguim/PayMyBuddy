package com.paymybuddy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

/**
 * Service handling user business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injection for UserRepository.
     *
     * @param userRepository repository used for user operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all users.
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return optional user
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Saves a user in database.
     *
     * @param user user to save
     * @return saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}