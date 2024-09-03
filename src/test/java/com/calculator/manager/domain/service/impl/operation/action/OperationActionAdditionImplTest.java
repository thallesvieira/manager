package com.calculator.manager.domain.service.impl.operation.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionAdditionImplTest {

    @InjectMocks
    private OperationActionAdditionImpl operationActionAdditionImpl;


    @Test
    void performsOperationAddition() {
        assertEquals(operationActionAdditionImpl.performsOperation(12.0, 12.0), "24.0");
        assertEquals(operationActionAdditionImpl.performsOperation(1.0, 12.0), "13.0");
        assertEquals(operationActionAdditionImpl.performsOperation(0.0, 0.0), "0.0");
    }
}