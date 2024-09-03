package com.calculator.manager.domain.service.impl.operation.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionSubtractionImplTest {
    @InjectMocks
    private OperationActionSubtractionImpl operationActionSubtractionImpl;


    @Test
    void performsOperationSubtraction() {
        assertEquals(operationActionSubtractionImpl.performsOperation(12.0, 12.0), "0.0");
        assertEquals(operationActionSubtractionImpl.performsOperation(1.0, 12.0), "-11.0");
        assertEquals(operationActionSubtractionImpl.performsOperation(0.0, 0.0), "0.0");
    }
}