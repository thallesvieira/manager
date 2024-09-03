package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.exception.ExceptionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionSquareRootImplTest {
    @InjectMocks
    private OperationActionSquareRootImpl operationActionSquareRoot;


    @Test
    void performsOperationSquareRoot() {
        assertEquals(operationActionSquareRoot.performsOperation(25.0, null), "5.0");
        assertEquals(operationActionSquareRoot.performsOperation(125.0, null), "11.180339887498949");
    }

    @Test
    void performsOperationSquareRoot_WhenHappensError() {
        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationActionSquareRoot.performsOperation(null, null);
        });

        assertEquals("Error to realize Square Root!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}