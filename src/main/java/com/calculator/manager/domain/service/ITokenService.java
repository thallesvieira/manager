package com.calculator.manager.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ITokenService {
    String createJwt(Authentication authentication);
    String retrieve(HttpServletRequest request);
    Boolean isValid(String token);
    Long getUserId(String token);
}
