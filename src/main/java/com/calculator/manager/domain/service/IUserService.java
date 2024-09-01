package com.calculator.manager.domain.service;

import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.model.user.User;

public interface IUserService {
    User getByUsername(String username);
    User getById(Long userId);
    void updateStatus(Long userId, StatusType status);
}
