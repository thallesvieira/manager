package com.calculator.manager.resource.persistence.repository.impl;


import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.repository.IUserRepository;
import com.calculator.manager.domain.service.impl.UserServiceImpl;
import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

/**
 * Repository to User entity
 */
@Repository
public class UserRepositoryImpl implements IUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    private IUserJPARepository userRepository;

    private ModelMapper mapper = new ModelMapper();

    /**
     * Find user entity by username and convert to user model to return to domain
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        logger.info("Getting user entity by username: {} and convert to model", username);
        return userRepository.findByUsername(username)
                .map(entity-> mapper.map(entity, User.class))
                .orElseThrow(() -> new ExceptionResponse("User not found!", HttpStatus.NOT_FOUND));
    }

    /**
     * Find user entity by id and convert to user model to return to domain
     * @param userId
     * @return
     */
    @Override
    public User findById(Long userId) {
        logger.info("Getting user entity by id: {} and convert to model", userId);
        return userRepository.findById(userId)
                .map(entity-> mapper.map(entity, User.class))
                .orElseThrow(() -> new ExceptionResponse("User not found!", HttpStatus.NOT_FOUND));
    }

    /**
     * update user entity and convert to model
     * @param user
     * @return
     */
    @Override
    public User update(User user) {
        try {
            logger.info("Maps user model to entity");
            UserEntity userEntity = mapper.map(user, UserEntity.class);

            logger.info("Update user entity");
            userEntity = userRepository.save(userEntity);

            logger.info("Convert user entity to model to return");
            return mapper.map(userEntity, User.class);
        } catch (Exception ex) {
            logger.error("Error to update user entity");
            throw new ExceptionResponse("Unable to update user", HttpStatus.BAD_REQUEST);
        }
    }
}
