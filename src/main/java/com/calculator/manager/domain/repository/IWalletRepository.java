package com.calculator.manager.domain.repository;

import com.calculator.manager.domain.model.user.Wallet;

public interface IWalletRepository {
    Wallet findByUserId(Long userId);
    Wallet updateWallet(Wallet wallet);
}
