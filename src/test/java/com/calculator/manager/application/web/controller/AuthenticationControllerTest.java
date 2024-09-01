package com.calculator.manager.application.web.controller;

import com.calculator.manager.domain.model.Login;
import com.calculator.manager.domain.model.Token;
import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.security.IAuthenticationFilter;
import com.calculator.manager.domain.service.ITokenService;
import com.calculator.manager.domain.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private IAuthenticationFilter auth;

    @Mock
    private IUserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ITokenService tokenService;

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");
        String mockToken = "Token";
        when(auth.authenticateLogin(login)).thenReturn(mockToken);

        ResponseEntity<?> response = authenticationController.login(login);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Token token = (Token) response.getBody();
        assertNotNull(token);
        assertEquals(mockToken, token.getToken());
        assertEquals("Bearer", token.getType());
        verify(auth).authenticateLogin(login);
    }

    @Test
    void login_ShouldThrowException_WhenAuthenticationFails() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        when(auth.authenticateLogin(login)).thenThrow(new RuntimeException("Authentication failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> authenticationController.login(login));
        assertEquals("Authentication failed", exception.getMessage());
        verify(auth).authenticateLogin(login);
    }

    @Test
    void signOut_ShouldReturnOk_WhenSignOutIsSuccessful() throws Exception {
        Long userId = 1L;

        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);

        doNothing().when(userService).updateStatus(userId, StatusType.INACTIVE);

        ResponseEntity<?> response = authenticationController.signOut();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(userService).updateStatus(userId, StatusType.INACTIVE);
    }

    @Test
    void signOut_ShouldThrowException_WhenSignOutFails() throws Exception {
        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenThrow(new RuntimeException("Error retrieving user id"));

        Exception exception = assertThrows(RuntimeException.class, () -> authenticationController.signOut());
        assertEquals("Error retrieving user id", exception.getMessage());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
    }
}