package com.calculator.manager.domain.repository;

import com.calculator.manager.domain.model.user.User;

public interface IUserRepository {
    User findByUsername(String username);
    User findById(Long userId);
    User update(User user);
}
