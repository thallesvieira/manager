package com.calculator.manager.domain.model.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Operation {
    Long id;
    OperationType type;
    Double cost;
}
