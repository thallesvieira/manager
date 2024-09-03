package com.calculator.manager.domain.service.impl.operation.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionMultiplicationImplTest {

    @InjectMocks
    private OperationActionMultiplicationImpl operationActionMultiplicationImpl;


    @Test
    void performsOperationMultiplication() {
        assertEquals(operationActionMultiplicationImpl.performsOperation(5.0, 5.0), "25.0");
        assertEquals(operationActionMultiplicationImpl.performsOperation(2.0, 12.0), "24.0");
        assertEquals(operationActionMultiplicationImpl.performsOperation(0.0, 0.0), "0.0");
    }
}