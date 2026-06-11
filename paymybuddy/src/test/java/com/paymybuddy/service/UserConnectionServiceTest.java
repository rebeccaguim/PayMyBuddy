package com.paymybuddy.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.UserConnectionRepository;

/**
 * Unit tests for UserConnectionService.
 */
@ExtendWith(MockitoExtension.class)
class UserConnectionServiceTest {

    @Mock
    private UserConnectionRepository userConnectionRepository;

    private UserConnectionService userConnectionService;

    private User user;
    private User friend;
    private UserConnection connection;

    @BeforeEach
    void setUp() {
        userConnectionService = new UserConnectionService(userConnectionRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("Rebecca");
        user.setEmail("rebecca@test.com");

        friend = new User();
        friend.setId(2L);
        friend.setUsername("Sarah");
        friend.setEmail("sarah@test.com");

        connection = new UserConnection();
        connection.setUser(user);
        connection.setFriend(friend);
    }

    @Test
    void getAllConnectionsShouldReturnConnections() {
        when(userConnectionRepository.findAll()).thenReturn(List.of(connection));

        List<UserConnection> result = userConnectionService.getAllConnections();

        assertEquals(1, result.size());
        assertEquals(friend, result.get(0).getFriend());
        verify(userConnectionRepository).findAll();
    }

    @Test
    void getConnectionsByUserShouldReturnUserConnections() {
        when(userConnectionRepository.findByUser(user)).thenReturn(List.of(connection));

        List<UserConnection> result = userConnectionService.getConnectionsByUser(user);

        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getUser());
        verify(userConnectionRepository).findByUser(user);
    }

    @Test
    void saveConnectionShouldSaveAndReturnConnection() {
        when(userConnectionRepository.save(connection)).thenReturn(connection);

        UserConnection result = userConnectionService.saveConnection(connection);

        assertSame(connection, result);
        verify(userConnectionRepository).save(connection);
    }

    @Test
    void addConnectionShouldCreateAndSaveConnection() {
        userConnectionService.addConnection(user, friend);

        ArgumentCaptor<UserConnection> connectionCaptor =
                ArgumentCaptor.forClass(UserConnection.class);

        verify(userConnectionRepository).save(connectionCaptor.capture());

        UserConnection savedConnection = connectionCaptor.getValue();

        assertEquals(user, savedConnection.getUser());
        assertEquals(friend, savedConnection.getFriend());
    }
}