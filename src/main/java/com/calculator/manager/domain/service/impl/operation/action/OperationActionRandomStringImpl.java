package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.gateway.IRandomString;
import com.calculator.manager.domain.service.IOperationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to generate a random string
 */
@Service
public class OperationActionRandomStringImpl implements IOperationAction {

    @Autowired
    private IRandomString randomString;

    /**
     * Method that realize random string operation.
     * This method call a gateway that generate the string and return it
     */
    @Override
    public String performsOperation(Double number1, Double number2) {
        return randomString.generateRandomString();
    }
}
