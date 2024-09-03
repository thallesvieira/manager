package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {
        "token.jwt.secret=testSecret",
        "token.jwt.expiration=3600000"
})
@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field secretField = TokenServiceImpl.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, "testSecret");

        Field expirationField = TokenServiceImpl.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(tokenService, "3600000");
    }

    @Test
    void createJwtSuccess() {
        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);

        String token = tokenService.createJwt(authentication);

        assertNotNull(token);
        Claims claims = Jwts.parser().setSigningKey("testSecret").parseClaimsJws(token).getBody();
        assertEquals("1", claims.getSubject());
        assertEquals("Api calculator", claims.getIssuer());
    }

    @Test
    void retrieveTokenSuccess() {
        String token = "Bearer testToken";
        when(request.getHeader("Authorization")).thenReturn(token);

        String result = tokenService.retrieve(request);

        assertNotNull(result);
        assertEquals("testToken", result);
    }

    @Test
    void retrieveTokenFailure_NoBearer() {
        String token = "testToken";
        when(request.getHeader("Authorization")).thenReturn(token);

        String result = tokenService.retrieve(request);

        assertNull(result);
    }

    @Test
    void isValidTokenSuccess() {
        String token = Jwts.builder()
                .setSubject("1")
                .signWith(SignatureAlgorithm.HS256, "testSecret")
                .compact();

        Boolean isValid = tokenService.isValid(token);

        assertTrue(isValid);
    }

    @Test
    void isValidTokenFailure() {
        String token = "invalidToken";

        Boolean isValid = tokenService.isValid(token);

        assertFalse(isValid);
    }

    @Test
    void getUserIdSuccess() {
        String token = Jwts.builder()
                .setSubject("1")
                .signWith(SignatureAlgorithm.HS256, "testSecret")
                .compact();

        Long userId = tokenService.getUserId(token);

        assertNotNull(userId);
        assertEquals(1L, userId);
    }

}