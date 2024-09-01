package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.service.IOperationAction;
import org.springframework.stereotype.Service;

/**
 * Class to realize addition operation
 */
@Service
public class OperationActionAdditionImpl implements IOperationAction {

    /**
     * Method that realize addition operation
     */
    @Override
    public String performsOperation(Double number1, Double number2) {
        return String.valueOf(number1 + number2);
    }
}
