package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.repository.IOperationRepository;
import com.calculator.manager.domain.service.IOperationAction;
import com.calculator.manager.domain.service.IRecordService;
import com.calculator.manager.domain.service.IUserService;
import com.calculator.manager.domain.service.IWalletService;
import com.calculator.manager.domain.validation.IOperationValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceImplTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private IOperationRepository operationRepository;

    @Mock
    private IRecordService recordService;

    @Mock
    private IWalletService walletService;

    @Mock
    private IUserService userService;

    @Mock
    private IOperationValidation operationValidation;

    @Mock
    private IOperationAction operationAction;

    @Test
    void realizeOperationSuccess() {
        Long userId = 1L;
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(10.0);
        operationFields.setNumber2(5.0);
        operationFields.setType(OperationType.ADDITION);

        Operation operation = new Operation();
        operation.setCost(1.0);
        operation.setType(OperationType.ADDITION);
        operationFields.getType().setOperationAction(operationAction);

        Wallet wallet = new Wallet();
        wallet.setBalance(10.0);

        User user = new User();
        user.setId(userId);

        when(operationRepository.findByType(OperationType.ADDITION)).thenReturn(operation);
        when(walletService.getByUserId(userId)).thenReturn(wallet);
        when(userService.getById(userId)).thenReturn(user);
        when(operationAction.performsOperation(10.0, 5.0)).thenReturn("15.0");

        String result = operationService.realizeOperation(userId, operationFields);

        assertEquals("15.0", result);
        verify(recordService).createRecord(operation, user, 9.0, "15.0");
        verify(walletService).updateBalance(wallet, 9.0);
    }

    @Test
    void realizeOperationInsufficientBalance() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(10.0);
        operationFields.setNumber2(5.0);
        operationFields.setType(OperationType.ADDITION);

        Operation operation = new Operation();
        operation.setId(1L);
        operation.setCost(20.0);
        operation.setType(OperationType.ADDITION);

        Wallet wallet = new Wallet();
        wallet.setBalance(10.0);

        when(operationRepository.findByType(OperationType.ADDITION)).thenReturn(operation);
        when(walletService.getByUserId(userId)).thenReturn(wallet);
        when(userService.getById(userId)).thenReturn(user);

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationService.realizeOperation(userId, operationFields);
        });

        assertEquals("Insufficient balance for this operation", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void getAllOperations() {
        Operation operation1 = new Operation();
        operation1.setId(1L);
        operation1.setCost(10.0);
        operation1.setType(OperationType.ADDITION);

        Operation operation2 = new Operation();
        operation2.setId(2L);
        operation2.setCost(20.0);
        operation2.setType(OperationType.SUBTRACTION);

        Operation operation3 = new Operation();
        operation3.setId(3L);
        operation3.setCost(30.0);
        operation3.setType(OperationType.MULTIPLICATION);

        List<Operation> operations = List.of(operation1, operation2, operation3);
        when(operationRepository.findAll()).thenReturn(operations);

        List<Operation> result = operationService.getOperations();

        assertEquals(operations, result);
        verify(operationRepository).findAll();
    }
}