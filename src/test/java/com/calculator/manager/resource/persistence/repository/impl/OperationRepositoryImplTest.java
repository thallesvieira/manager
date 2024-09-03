package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.resource.persistence.entity.OperationEntity;
import com.calculator.manager.resource.persistence.repository.IOperationJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OperationRepositoryImplTest {

    @InjectMocks
    private OperationRepositoryImpl operationRepositoryImpl;

    @Mock
    private IOperationJPARepository operationRepository;

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByType_Success() {
        OperationType type = OperationType.ADDITION;

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setType(type);

        Operation operation = new Operation();
        operation.setType(type);

        when(operationRepository.findByType(type)).thenReturn(Optional.of(operationEntity));
        when(mapper.map(operationEntity, Operation.class)).thenReturn(operation);

        Operation result = operationRepositoryImpl.findByType(type);

        assertNotNull(result);
        assertEquals(type, result.getType());
    }

    @Test
    void findByType_NotFound() {
        OperationType type = OperationType.SUBTRACTION;

        when(operationRepository.findByType(type)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> {
            operationRepositoryImpl.findByType(type);
        });

        assertEquals("Operation not found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void findAll_Success() {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setType(OperationType.MULTIPLICATION);

        OperationEntity operationEntity2 = new OperationEntity();
        operationEntity2.setType(OperationType.SUBTRACTION);

        Operation operation = new Operation();
        operation.setType(OperationType.MULTIPLICATION);

        Operation operation2 = new Operation();
        operation2.setType(OperationType.SUBTRACTION);

        when(operationRepository.findAll()).thenReturn(Arrays.asList(operationEntity, operationEntity2));
        when(mapper.map(operationEntity, Operation.class)).thenReturn(operation);
        when(mapper.map(operationEntity2, Operation.class)).thenReturn(operation2);

        List<Operation> result = operationRepositoryImpl.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(OperationType.MULTIPLICATION, result.get(0).getType());
        assertEquals(OperationType.SUBTRACTION, result.get(1).getType());
    }

    @Test
    void findAll_EmptyList() {
        when(operationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Operation> result = operationRepositoryImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}