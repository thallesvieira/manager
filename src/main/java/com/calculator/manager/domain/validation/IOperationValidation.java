package com.calculator.manager.domain.validation;

import com.calculator.manager.domain.model.OperationFields;
import org.springframework.stereotype.Component;

@Component
public interface IOperationValidation {
    void validate(OperationFields operationFields);
}
