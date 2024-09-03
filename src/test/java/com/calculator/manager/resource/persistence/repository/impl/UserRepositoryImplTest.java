package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private IUserJPARepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;


    @Test
    void findByUsername_Success() {
        String username = "username";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        User result = userRepositoryImpl.findByUsername(username);

        assertNotNull(result);
        assertEquals(result, user);
    }

    @Test
    void findByUsername_NotFound() {
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> userRepositoryImpl.findByUsername(username));
        assertEquals("User not found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void findById_Success() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        User result = userRepositoryImpl.findById(userId);

        assertNotNull(result);
        assertEquals(result, user);
    }

    @Test
    void findById_NotFound() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> userRepositoryImpl.findById(userId));
        assertEquals("User not found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void update_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setId(1L);
        userEntity.setStatus(StatusType.INACTIVE);

        User user = new User();
        user.setUsername("username");
        user.setId(1L);
        user.setStatus(StatusType.ACTIVE);

        User userInactive = new User();
        userInactive.setUsername("username");
        userInactive.setId(1L);
        userInactive.setStatus(StatusType.ACTIVE);

        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.map(userEntity, User.class)).thenReturn(userInactive);

        User result = userRepositoryImpl.update(user);

        assertNotNull(result);
        assertEquals(result, userInactive);
    }

    @Test
    void update_Exception() {
        User user = new User();

        when(mapper.map(user, UserEntity.class)).thenThrow(new RuntimeException("Mapping error"));

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> userRepositoryImpl.update(user));
        assertEquals("Unable to update user", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}