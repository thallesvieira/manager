package com.calculator.manager.resource.gateway.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.resource.gateway.IRandomStringFeign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomStringGatewayImplTest {

    @InjectMocks
    private RandomStringGatewayImpl randomStringGateway;

    @Mock
    private IRandomStringFeign randomStringFeign;

    @Test
    void generateRandomString_Success() {
        String expectedRandomString = "HJGFNDJKLADGHTEA";
        when(randomStringFeign.getRandomStrings(
                1,
                16,
                "off",
                "on",
                "off",
                "on",
                "plain",
                "new")).thenReturn(expectedRandomString);

        String actualRandomString = randomStringGateway.generateRandomString();

        assertNotNull(actualRandomString);
        assertEquals(expectedRandomString, actualRandomString.trim());
    }

    @Test
    void generateRandomString_ExceptionThrown() {
        when(randomStringFeign.getRandomStrings(
                1,
                16,
                "off",
                "on",
                "off",
                "on",
                "plain",
                "new")).thenThrow(new RuntimeException("Feign client error"));

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            randomStringGateway.generateRandomString();
        });

        assertEquals("Unable to generate a random string", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}