package com.paymybuddy.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

/**
 * Unit tests for UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);

        user = new User();
        user.setId(1L);
        user.setUsername("Rebecca");
        user.setEmail("rebecca@test.com");
        user.setPassword("plainPassword");
        user.setBalance(100.0);
    }

    @Test
    void getAllUsersShouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Rebecca", result.get(0).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void getUserByIdShouldReturnUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertEquals("Rebecca", result.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByEmailShouldReturnUserWhenFound() {
        when(userRepository.findByEmail("rebecca@test.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("rebecca@test.com");

        assertEquals("rebecca@test.com", result.get().getEmail());
        verify(userRepository).findByEmail("rebecca@test.com");
    }

    @Test
    void getUserByEmailShouldReturnEmptyWhenNotFound() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("unknown@test.com");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("unknown@test.com");
    }

    @Test
    void saveUserShouldEncodePasswordBeforeSaving() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertSame(user, result);

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(user);
    }

    @Test
    void updateProfileShouldUpdateUsernameEmailAndPasswordWhenPasswordIsProvided() {
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updateProfile(user, "Rebecca Updated", "updated@test.com", "newPassword");

        assertEquals("Rebecca Updated", user.getUsername());
        assertEquals("updated@test.com", user.getEmail());
        assertEquals("encodedNewPassword", user.getPassword());

        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(user);
    }

    @Test
    void updateProfileShouldNotUpdatePasswordWhenPasswordIsBlank() {
        user.setPassword("existingEncodedPassword");

        userService.updateProfile(user, "Rebecca Updated", "updated@test.com", "");

        assertEquals("Rebecca Updated", user.getUsername());
        assertEquals("updated@test.com", user.getEmail());
        assertEquals("existingEncodedPassword", user.getPassword());

        verify(passwordEncoder, never()).encode("");
        verify(userRepository).save(user);
    }
}