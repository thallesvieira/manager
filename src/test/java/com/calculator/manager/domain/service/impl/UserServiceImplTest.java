package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private IUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setStatus(StatusType.ACTIVE);
    }

    @Test
    void getByUsername() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(user);

        User result = userService.getByUsername(username);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getById() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(user);

        User result = userService.getById(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateStatus() {
        Long userId = 1L;
        StatusType newStatus = StatusType.ACTIVE;
        StatusType currentStatus = StatusType.INACTIVE;
        user.setStatus(currentStatus);

        when(userRepository.findById(userId)).thenReturn(user);

        userService.updateStatus(userId, newStatus);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void updateStatusNoChange() {
        Long userId = 1L;
        StatusType status = StatusType.INACTIVE;

        when(userRepository.findById(userId)).thenReturn(user);

        userService.updateStatus(userId, status);

        verify(userRepository, times(1)).findById(userId);
    }
}