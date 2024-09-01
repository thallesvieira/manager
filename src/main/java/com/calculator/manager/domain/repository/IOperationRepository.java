package com.calculator.manager.domain.repository;

import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;

import java.util.List;

public interface IOperationRepository {
    Operation findByType(OperationType type);
    List<Operation> findAll();
}
