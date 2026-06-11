package com.paymybuddy.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;

/**
 * Unit tests for CustomUserDetailsService.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("Rebecca");
        user.setEmail("rebecca@test.com");
        user.setPassword("encodedPassword");
        user.setBalance(100.0);
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        when(userRepository.findByEmail("rebecca@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService.loadUserByUsername("rebecca@test.com");

        assertEquals("rebecca@test.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1, result.getAuthorities().size());

        verify(userRepository).findByEmail("rebecca@test.com");
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown@test.com")
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("unknown@test.com");
    }
}