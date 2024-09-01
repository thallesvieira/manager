package com.calculator.manager.domain.service;

import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.Operation;

import java.util.List;

public interface IOperationService {
    String operation(Long userId, OperationFields operationFields);
    List<Operation> getOperations();
}
