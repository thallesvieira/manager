package com.calculator.manager.domain.validation.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.OperationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationValidationTest {

    @InjectMocks
    private OperationValidation operationValidation;

    @Test
    void validate_WhenIsOK() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(1.0);
        operationFields.setNumber2(12.0);
        operationFields.setType(OperationType.ADDITION);

        operationValidation.validate(operationFields);
    }

    @Test
    void validate_WhenNumberIsNull() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(null);
        operationFields.setNumber2(12.0);
        operationFields.setType(OperationType.ADDITION);

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationValidation.validate(operationFields);
        });

        assertEquals("Number is null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void validate_WhenNumberIsNegative() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(-15.0);
        operationFields.setNumber2(12.0);
        operationFields.setType(OperationType.SQUARE_ROOT);


        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationValidation.validate(operationFields);
        });

        assertEquals("Cannot calculate the square root of a negative number", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void validate_WhenNumberIsZero() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(15.0);
        operationFields.setNumber2(0.0);
        operationFields.setType(OperationType.DIVISION);


        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationValidation.validate(operationFields);
        });

        assertEquals("Cannot calculate division by number zero", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}