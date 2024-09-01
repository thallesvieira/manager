package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.repository.IWalletRepository;
import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.entity.WalletEntity;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import com.calculator.manager.resource.persistence.repository.IWalletJPARepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Class to WalletEntity, where is the user's balance
 */
@Repository
public class WalletRepositoryImpl implements IWalletRepository {
    private static final Logger logger = LoggerFactory.getLogger(WalletRepositoryImpl.class);

    @Autowired
    private IWalletJPARepository walletJPARepository;

    @Autowired
    private IUserJPARepository userJPARepository;

    private ModelMapper mapper = new ModelMapper();

    /**
     * Find wallet by user id
     * @param userId
     * @return
     */
    @Override
    public Wallet findByUserId(Long userId) {
        logger.info("Getting wallet entity by user id: {}", userId);
        Optional<UserEntity> userEntity = userJPARepository.findById(userId);

        logger.info("Convert wallet entity to model");
        return walletJPARepository.findByUser(userEntity.get())
                .map(entity-> mapper.map(entity, Wallet.class))
                .orElseThrow(()-> new ExceptionResponse("Wallet not found!", HttpStatus.NOT_FOUND));
    }

    /**
     * update new balance from wallet
     * @param wallet
     * @return
     */
    @Override
    public Wallet updateWallet(Wallet wallet) {
        try {
            logger.info("Maps wallet model to entity");
            WalletEntity walletEntity = mapper.map(wallet, WalletEntity.class);

            logger.info("Update wallet entity");
            walletEntity =  walletJPARepository.save(walletEntity);

            logger.info("Maps wallet entity to model and return");
            return mapper.map(walletEntity, Wallet.class);
        } catch(Exception ex){
            logger.error("Error to update wallet entity");
            throw new ExceptionResponse("Unable to update wallet", HttpStatus.BAD_REQUEST);
        }
    }
}
