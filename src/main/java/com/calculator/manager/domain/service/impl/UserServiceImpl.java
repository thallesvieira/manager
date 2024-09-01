package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.repository.IUserRepository;
import com.calculator.manager.domain.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible to actions about user
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserRepository userRepository;

    /**
     * Get user by username
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        logger.info("Getting user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    @Override
    public User getById(Long userId) {
        logger.info("Getting user by id: {}", userId);
        return userRepository.findById(userId);
    }

    /**
     * Method to activate or inactivate user after generate the token or when realize log out.
     * Check if is necessary update status
     * @param userId
     */
    @Override
    public void updateStatus(Long userId, StatusType status) {
        logger.info("Getting user by id: {} to update status to {}", userId, status);
        User user = userRepository.findById(userId);

        if (user.getStatus() != status) {
            user.setStatus(status);

            logger.info("Update status from user {} to {}", userId, status);
            userRepository.update(user);
        }
    }
}
