package com.calculator.manager.application.web.controller;

import com.calculator.manager.application.web.dto.response.RecordDTOResponse;
import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.service.IRecordService;
import com.calculator.manager.domain.service.ITokenService;
import com.calculator.manager.domain.service.IWalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IWalletService walletService;

    @Mock
    private IRecordService recordService;

    @Mock
    private ITokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Test
    void getUserBalance_ShouldReturnUserBalance_WhenSuccessful() {
        Long userId = 1L;
        double balance = 100.0;
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(balance);
        wallet.setUser(user);

        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(walletService.getByUserId(userId)).thenReturn(wallet);

        ResponseEntity<?> response = userController.getUserBalance();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("$100.00", response.getBody());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(walletService).getByUserId(userId);
    }

    @Test
    void getUserBalance_ShouldThrowException_WhenFails() {
        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenThrow(new RuntimeException("Failed to retrieve user id"));

        Exception exception = assertThrows(RuntimeException.class, () -> userController.getUserBalance());
        assertEquals("Failed to retrieve user id", exception.getMessage());
    }

    @Test
    void getUserRecords_ShouldReturnPageOfRecords_WhenSuccessful() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("username");

        Operation operation1 = new Operation();
        operation1.setId(1L);
        operation1.setType(OperationType.ADDITION);
        operation1.setCost(10.0);

        Operation operation2 = new Operation();
        operation2.setId(2L);
        operation2.setType(OperationType.SUBTRACTION);
        operation2.setCost(5.0);

        Record record1 = new Record();
        record1.setId(1L);
        record1.setUser(user);
        record1.setOperation(operation1);
        record1.setAmount(10.0);
        record1.setUserBalance(100.0);
        record1.setOperationResponse("15.00");
        record1.setDate(LocalDateTime.now());
        record1.setInactive(false);

        Record record2 = new Record();
        record2.setId(2L);
        record2.setUser(user);
        record2.setOperation(operation2);
        record2.setAmount(5.0);
        record2.setUserBalance(80.0);
        record2.setOperationResponse("15.00");
        record2.setDate(LocalDateTime.now());
        record2.setInactive(false);

        List<Record> records = Arrays.asList(record1, record2);
        Page<Record> page = new PageImpl<>(records, PageRequest.of(0, 10), records.size());

        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(recordService.getRecords(null, 0, 10, userId, false)).thenReturn(page);

        ResponseEntity<?> response = userController.getUserRecords(null, 0, 10, false);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Page<RecordDTOResponse> responseBody = (Page<RecordDTOResponse>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.getTotalElements());

        RecordDTOResponse dto1 = responseBody.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("$10.00", dto1.getAmount());
        assertEquals("$100.00", dto1.getBalance());

        RecordDTOResponse dto2 = responseBody.getContent().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("$5.00", dto2.getAmount());
        assertEquals("$80.00", dto2.getBalance());

        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(recordService).getRecords(null, 0, 10, userId, false);
    }

    @Test
    void getUserRecords_ShouldThrowException_WhenFails() {
        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenThrow(new RuntimeException("Failed to retrieve user id"));

        Exception exception = assertThrows(RuntimeException.class, () -> userController.getUserRecords(null, 0, 10, false));
        assertEquals("Failed to retrieve user id", exception.getMessage());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
    }

    @Test
    void inactivateUserRecords_ShouldReturnUpdatedRecord_WhenSuccessful() {
        Long userId = 1L;
        Long recordId = 1L;
        Boolean inactive = true;

        User user = new User();
        user.setId(userId);
        user.setUsername("username");

        Operation operation = new Operation();
        operation.setId(recordId);
        operation.setType(OperationType.ADDITION);
        operation.setCost(10.0);

        Record record = new Record();
        record.setId(1L);
        record.setUser(user);
        record.setOperation(operation);
        record.setAmount(10.0);
        record.setUserBalance(100.0);
        record.setDate(LocalDateTime.now());
        record.setOperationResponse("10.00");
        record.setInactive(inactive);

        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(recordService.inativateRecord(inactive, userId, recordId)).thenReturn(record);

        ResponseEntity<?> response = userController.inactivateUserRecords(recordId, inactive);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        RecordDTOResponse responseBody = (RecordDTOResponse) response.getBody();
        assertNotNull(responseBody);
        assertEquals(recordId, responseBody.getId());
        assertEquals("$10.00", responseBody.getAmount());
        assertEquals("$100.00", responseBody.getBalance());
        assertEquals(inactive, responseBody.getInactive());

        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(recordService).inativateRecord(inactive, userId, recordId);
    }

    @Test
    void inactivateUserRecords_ShouldThrowException_WhenFails() {
        Long userId = 1L;
        Long recordId = 1L;
        Boolean inactive = true;

        when(tokenService.retrieve(request)).thenReturn("token");
        when(tokenService.getUserId("token")).thenReturn(userId);
        when(recordService.inativateRecord(inactive, userId, recordId)).thenThrow(new RuntimeException("Failed to inactivate record"));

        Exception exception = assertThrows(RuntimeException.class, () -> userController.inactivateUserRecords(recordId, inactive));
        assertEquals("Failed to inactivate record", exception.getMessage());
        verify(tokenService).retrieve(request);
        verify(tokenService).getUserId("token");
        verify(recordService).inativateRecord(inactive, userId, recordId);
    }
}