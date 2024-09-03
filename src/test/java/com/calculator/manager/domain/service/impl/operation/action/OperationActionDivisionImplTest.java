package com.calculator.manager.domain.service.impl.operation.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionDivisionImplTest {
    @InjectMocks
    private OperationActionDivisionImpl operationActionDivisionImpl;


    @Test
    void performsOperationDivision() {
        assertEquals(operationActionDivisionImpl.performsOperation(12.0, 2.0), "6.0");
        assertEquals(operationActionDivisionImpl.performsOperation(5.0, 2.0), "2.5");
    }
}