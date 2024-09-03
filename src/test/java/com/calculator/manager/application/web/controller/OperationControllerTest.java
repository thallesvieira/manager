package com.calculator.manager.application.web.controller;

import com.calculator.manager.application.web.dto.response.OperationDTOResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.service.IOperationService;
import com.calculator.manager.domain.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationControllerTest {
    @InjectMocks
    private OperationController operationController;

    @Mock
    private IOperationService operationService;

    @Mock
    private ITokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Test
    void saveOperation_ShouldReturnOk_WhenOperationIsSuccessful() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(10.0);
        operationFields.setNumber2(5.0);
        operationFields.setType(OperationType.ADDITION);

        Long userId = 1L;
        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(operationService.realizeOperation(userId, operationFields)).thenReturn("15.0");

        ResponseEntity<?> response = operationController.saveOperation(operationFields);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("15.0", response.getBody());
    }

    @Test
    void saveOperation_ShouldThrowException_WhenOperationFails() {
        OperationFields operationFields = new OperationFields();
        operationFields.setNumber1(12.0);
        operationFields.setNumber2(5.0);
        operationFields.setType(OperationType.ADDITION);

        Long userId = 1L;
        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(operationService.realizeOperation(userId, operationFields)).thenThrow(new RuntimeException("Operation failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> operationController.saveOperation(operationFields));
        assertEquals("Operation failed", exception.getMessage());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(operationService).realizeOperation(userId, operationFields);
    }

    @Test
    void getOperations_ShouldReturnListOfOperations_WhenOperationsExist() {
        Operation operation1 = new Operation();
        operation1.setId(1L);
        operation1.setType(OperationType.ADDITION);
        operation1.setCost(10.0);

        Operation operation2 = new Operation();
        operation2.setId(2L);
        operation2.setType(OperationType.SUBTRACTION);
        operation2.setCost(5.0);

        when(operationService.getOperations()).thenReturn(Arrays.asList(operation1, operation2));

        ResponseEntity<?> response = operationController.getOperations();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<OperationDTOResponse> responseBody = (List<OperationDTOResponse>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        OperationDTOResponse dto1 = responseBody.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals(OperationType.ADDITION, dto1.getType());
        assertEquals("ADDITION", dto1.getName());
        assertEquals("$10.00", dto1.getCost());

        OperationDTOResponse dto2 = responseBody.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals(OperationType.SUBTRACTION, dto2.getType());
        assertEquals("SUBTRACTION", dto2.getName());
        assertEquals("$5.00", dto2.getCost());

        verify(operationService).getOperations();
    }

    @Test
    void getOperations_ShouldThrowException_WhenGetOperationsFails() {
        when(operationService.getOperations()).thenThrow(new RuntimeException("Failed to get operations"));

        Exception exception = assertThrows(RuntimeException.class, () -> operationController.getOperations());
        assertEquals("Failed to get operations", exception.getMessage());
        verify(operationService).getOperations();
    }
}