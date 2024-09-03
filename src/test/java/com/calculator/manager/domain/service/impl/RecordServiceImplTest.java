package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.repository.IRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @InjectMocks
    private RecordServiceImpl recordService;

    @Mock
    private IRecordRepository recordRepository;


    @Test
    void createRecordSuccess() {
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(OperationType.SUBTRACTION);
        operation.setCost(5.0);

        User user = new User();
        user.setId(1L);

        Record record = new Record();
        record.setOperation(operation);
        record.setUser(user);
        record.setAmount(5.0);
        record.setUserBalance(45.0);
        record.setOperationResponse("10.0");
        record.setDate(LocalDateTime.now());
        record.setInactive(false);

        when(recordRepository.save(any(Record.class))).thenReturn(record);

        Record result = recordService.createRecord(operation, user, 45.0, "10.0");

        assertNotNull(result);
        assertEquals(operation, result.getOperation());
        assertEquals(user, result.getUser());
        assertEquals(5.0, result.getAmount());
        assertEquals(45.0, result.getUserBalance());
        assertEquals("10.0", result.getOperationResponse());
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void inactivateRecordSuccess() {
        Long userId = 1L;
        Long recordId = 1L;
        Record record = new Record();
        record.setInactive(false);

        when(recordRepository.findByIdAndUserId(recordId, userId)).thenReturn(record);
        when(recordRepository.save(record)).thenReturn(record);

        Record result = recordService.inativateRecord(true, userId, recordId);

        assertNotNull(result);
        assertTrue(result.getInactive());
        verify(recordRepository, times(1)).findByIdAndUserId(recordId, userId);
        verify(recordRepository, times(1)).save(record);
    }

    @Test
    void getRecordsSuccess() {
        String search = "test";
        int page = 0;
        int pageSize = 10;
        Long userId = 1L;
        Boolean inactive = false;

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"));
        Record record = new Record();
        Page<Record> pageRecords = new PageImpl<>(Collections.singletonList(record));

        when(recordRepository.findAllByUserIdAndInactive(search, pageable, userId, inactive))
                .thenReturn(pageRecords);

        Page<Record> result = recordService.getRecords(search, page, pageSize, userId, inactive);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(recordRepository, times(1)).findAllByUserIdAndInactive(search, pageable, userId, inactive);
    }
}