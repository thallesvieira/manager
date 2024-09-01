package com.calculator.manager.domain.validation.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.validation.IOperationValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to validate numbers depending on operation.
 * Each operation has your validation.
 * The method identify the operation and call private methods to validate
 */
@Component
public class OperationValidation implements IOperationValidation {
    private static final Logger logger = LoggerFactory.getLogger(OperationValidation.class);

    @Override
    public void validate(OperationFields operationFields) {
        List<OperationType> commonOperations = new ArrayList<>();
        commonOperations.add(OperationType.ADDITION);
        commonOperations.add(OperationType.SUBTRACTION);
        commonOperations.add(OperationType.DIVISION);
        commonOperations.add(OperationType.MULTIPLICATION);


        logger.info("Validate new operation with type: {}, number1 {} and number2 {}",
                operationFields.getType(), operationFields.getNumber1(), operationFields.getNumber2());

        if (commonOperations.contains(operationFields.getType())) {
            validValidNumber(operationFields.getNumber1());
            validValidNumber(operationFields.getNumber2());
        }

        if (operationFields.getType().equals(OperationType.SQUARE_ROOT)) {
            validValidNumber(operationFields.getNumber1());
            validNegativeNumber(operationFields.getNumber1());
        }

        if (operationFields.getType().equals(OperationType.DIVISION)) {
            validNumberZero(operationFields.getNumber2());
        }
    }

    private void validValidNumber(Double number) {
        logger.info("Validate if number {} is null", number);
        if (number == null)
            throw new ExceptionResponse("Number is null", HttpStatus.BAD_REQUEST);

        logger.info("Validate if number {} is not a number", number);
        if (number.isNaN())
            throw new ExceptionResponse("Number " + number + " is invalid", HttpStatus.BAD_REQUEST);
    }

    private void validNumberZero(Double number) {
        logger.info("Validate if number {} is zero", number);
        if (number == 0)
            throw new ExceptionResponse("Cannot calculate division by number zero", HttpStatus.BAD_REQUEST);
    }

    private void validNegativeNumber(Double number) {
        logger.info("Validate if number {} is less that zero", number);
        if (number < 0)
            throw new ExceptionResponse("Cannot calculate the square root of a negative number", HttpStatus.BAD_REQUEST);
    }
}
