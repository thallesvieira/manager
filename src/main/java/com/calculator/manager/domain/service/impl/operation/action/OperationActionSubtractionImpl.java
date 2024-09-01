package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.service.IOperationAction;
import org.springframework.stereotype.Service;

/**
 * Class to realize subtraction operation
 */
@Service
public class OperationActionSubtractionImpl implements IOperationAction {

    /**
     * Method that realize subtraction operation
     */
    @Override
    public String performsOperation(Double number1, Double number2) {
        return String.valueOf(number1 - number2);
    }
}
