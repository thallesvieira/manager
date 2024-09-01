package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.service.IOperationAction;
import org.springframework.stereotype.Service;

/**
 * Class to realize multiplication operation.
 */
@Service
public class OperationActionMultiplicationImpl implements IOperationAction {

    /**
     * Method that realize multiplication operation.
     */
    @Override
    public String performsOperation(Double number1, Double number2) {
        return String.valueOf(number1*number2);
    }
}
