package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.repository.IWalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private IWalletRepository walletRepository;

    @Test
    void getByUserId() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Wallet wallet = new Wallet();
        wallet.setBalance(10.0);
        wallet.setUser(user);

        when(walletRepository.findByUserId(userId)).thenReturn(wallet);

        Wallet result = walletService.getByUserId(userId);

        assertNotNull(result);
        assertEquals(result, wallet);
    }

    @Test
    void updateBalance() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Wallet wallet = new Wallet();
        wallet.setBalance(10.0);
        wallet.setUser(user);
        Double newBalance = 100.0;

        when(walletRepository.updateWallet(wallet)).thenReturn(wallet);

        Wallet updatedWallet = walletService.updateBalance(wallet, newBalance);

        assertNotNull(updatedWallet);
        assertEquals(newBalance, updatedWallet.getBalance());
    }
}