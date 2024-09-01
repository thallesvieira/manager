package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.repository.IWalletRepository;
import com.calculator.manager.domain.service.IWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to control the balance from user.
 */
@Service
public class WalletServiceImpl implements IWalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private IWalletRepository walletRepository;

    /**
     * Get wallet by user
     *
     * @param userId
     * @return
     */
    @Override
    public Wallet getByUserId(Long userId) {
        logger.info("Getting wallet by user id: {}", userId);
        return walletRepository.findByUserId(userId);
    }

    /**
     * Update new balance to user.
     *
     * @param wallet
     * @param balance
     * @return
     */
    @Override
    @Transactional
    public Wallet updateBalance(Wallet wallet, Double balance) {
        wallet.setBalance(balance);
        logger.info("Update new balance from user: {}", wallet.getUser().getId());
        return walletRepository.updateWallet(wallet);
    }
}
