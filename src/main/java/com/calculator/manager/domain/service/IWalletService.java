package com.calculator.manager.domain.service;

import com.calculator.manager.domain.model.user.Wallet;

public interface IWalletService {
    Wallet getByUserId(Long userId);
    Wallet updateBalance(Wallet wallet, Double balance);
}
