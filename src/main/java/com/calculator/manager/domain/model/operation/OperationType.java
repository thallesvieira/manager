package com.calculator.manager.domain.model.operation;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.service.IOperationAction;
import com.calculator.manager.domain.service.impl.operation.action.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * Enum with operation types
 */
public enum OperationType {
    ADDITION("ADDITION"),
    SUBTRACTION("SUBTRACTION"),
    MULTIPLICATION("MULTIPLICATION"),
    DIVISION("DIVISION"),
    SQUARE_ROOT("SQUARE ROOT"),
    RANDOM_STRING("RANDOM STRING");

    @Getter
    String name;
    OperationType(String name) {
        this.name = name;
    }

    /**
     * Class to instantiate each class that do the operation.
     * If other operation type to appear just put the new type
     * and call the class that performs the operation.
     */
    @Component
    public static class OperationTypeInjector {

        @Autowired
        private OperationActionAdditionImpl operationActionAddition;

        @Autowired
        private OperationActionSubtractionImpl operationActionSubtraction;

        @Autowired
        private OperationActionDivisionImpl operationActionDivision;

        @Autowired
        private OperationActionMultiplicationImpl operationActionMultiplication;

        @Autowired
        private OperationActionSquareRootImpl operationActionSquareRoot;

        @Autowired
        private OperationActionRandomStringImpl operationActionRandomString;

        /**
         * This constructor set to instantiate class from each operation.
         * With this we have a scalability for new operation types
         * and it's possible to call the class using this types
         */
        @PostConstruct
        public void postConstructor() {
            for (OperationType operationType : EnumSet.allOf(OperationType.class)) {
                switch (operationType) {
                    case ADDITION -> {
                        operationType.setOperationAction(operationActionAddition);
                    }
                    case SUBTRACTION -> {
                        operationType.setOperationAction(operationActionSubtraction);
                    }
                    case MULTIPLICATION -> {
                        operationType.setOperationAction(operationActionMultiplication);
                    }
                    case DIVISION -> {
                        operationType.setOperationAction(operationActionDivision);
                    }
                    case RANDOM_STRING -> {
                        operationType.setOperationAction(operationActionRandomString);
                    }
                    case SQUARE_ROOT -> {
                        operationType.setOperationAction(operationActionSquareRoot);
                    }
                    default -> {
                        throw new ExceptionResponse("Operation type not found", HttpStatus.NOT_FOUND);
                    }
                }
            }
        }
    }

    /**
     * Set an instantiated operation action.
     */
    public IOperationAction operationAction;
    public void setOperationAction(IOperationAction operationAction) {
        this.operationAction = operationAction;
    }
}
