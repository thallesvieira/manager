package com.calculator.manager.domain.model;

import com.calculator.manager.domain.model.operation.OperationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationFields {
    Double number1;
    Double number2;
    OperationType type;
}
