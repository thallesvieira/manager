package com.calculator.manager.domain.security;

import com.calculator.manager.domain.model.Login;

public interface IAuthenticationFilter {
    String authenticateLogin(Login login);
}
