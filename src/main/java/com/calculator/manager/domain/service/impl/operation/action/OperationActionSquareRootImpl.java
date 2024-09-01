package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.service.IOperationAction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Class to realize square root operation
 */
@Service
public class OperationActionSquareRootImpl implements IOperationAction {

    /**
     * Method that realize square root operation.
     * Here exist a Try Catch for validate the operation that is more complexity.
     */
    @Override
    public String performsOperation(Double number1, Double number2) {
        try {
            return String.valueOf(Math.sqrt(number1));
        }catch (Exception ex) {
            throw new ExceptionResponse("Error to realize Square Root!", HttpStatus.BAD_REQUEST);
        }
    }
}
