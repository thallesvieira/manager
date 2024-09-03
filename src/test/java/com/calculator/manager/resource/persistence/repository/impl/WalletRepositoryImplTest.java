package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.entity.WalletEntity;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import com.calculator.manager.resource.persistence.repository.IWalletJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryImplTest {

    @Mock
    private IWalletJPARepository walletJPARepository;

    @Mock
    private IUserJPARepository userJPARepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private WalletRepositoryImpl walletRepositoryImpl;


    @Test
    void findByUserId_Success() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setId(1L);

        when(userJPARepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(walletJPARepository.findByUser(userEntity)).thenReturn(Optional.of(walletEntity));
        when(mapper.map(walletEntity, Wallet.class)).thenReturn(wallet);

        Wallet result = walletRepositoryImpl.findByUserId(userId);

        assertNotNull(result);
        assertEquals(result, wallet);
    }

    @Test
    void findByUserId_UserNotFound() {
        Long userId = 999L;

        when(userJPARepository.findById(userId)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> walletRepositoryImpl.findByUserId(userId));
        assertEquals("User not found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void findByUserId_WalletNotFound() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();

        when(userJPARepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(walletJPARepository.findByUser(userEntity)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> walletRepositoryImpl.findByUserId(userId));
        assertEquals("Wallet not found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateWallet_Success() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.00);

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setBalance(100.00);

        when(mapper.map(wallet, WalletEntity.class)).thenReturn(walletEntity);
        when(walletJPARepository.save(walletEntity)).thenReturn(walletEntity);
        when(mapper.map(walletEntity, Wallet.class)).thenReturn(wallet);

        Wallet result = walletRepositoryImpl.updateWallet(wallet);

        assertNotNull(result);
        assertEquals(result, wallet);
    }

    @Test
    void updateWallet_Exception() {
        Wallet wallet = new Wallet();

        when(mapper.map(wallet, WalletEntity.class)).thenThrow(new RuntimeException("Mapping error"));

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> walletRepositoryImpl.updateWallet(wallet));
        assertEquals("Unable to update wallet", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}