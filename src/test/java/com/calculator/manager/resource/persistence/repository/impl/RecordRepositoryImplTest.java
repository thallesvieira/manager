package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.resource.persistence.entity.OperationEntity;
import com.calculator.manager.resource.persistence.entity.RecordEntity;
import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.repository.IOperationJPARepository;
import com.calculator.manager.resource.persistence.repository.IRecordJPARepository;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordRepositoryImplTest {

    @Mock
    private IRecordJPARepository recordJPARepository;

    @Mock
    private IOperationJPARepository operationJPARepository;

    @Mock
    private IUserJPARepository userJPARepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private RecordRepositoryImpl recordRepository;

    @Test
    void save_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setId(1L);
        operationEntity.setType(OperationType.ADDITION);

        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(OperationType.ADDITION);


        Record record = new Record();
        record.setId(1L);
        record.setUser(user);
        record.setOperation(operation);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(1L);
        recordEntity.setUser(userEntity);
        recordEntity.setOperation(operationEntity);

        when(mapper.map(record, RecordEntity.class)).thenReturn(recordEntity);
        when(operationJPARepository.findById(1L)).thenReturn(Optional.of(recordEntity.getOperation()));
        when(userJPARepository.findById(1L)).thenReturn(Optional.of(recordEntity.getUser()));
        when(recordJPARepository.save(recordEntity)).thenReturn(recordEntity);
        when(mapper.map(recordEntity, Record.class)).thenReturn(record);

        Record result = recordRepository.save(record);

        assertNotNull(result);
        verify(mapper).map(record, RecordEntity.class);
        verify(recordJPARepository).save(recordEntity);
        verify(mapper).map(recordEntity, Record.class);
    }

    @Test
    void save_Exception() {
        Record record = new Record();

        when(mapper.map(record, RecordEntity.class)).thenThrow(new RuntimeException("Error during mapping"));

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> recordRepository.save(record));
        assertEquals("Unable to save the record", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void findByIdAndUserId_Success() {
        Long recordId = 1L;
        Long userId = 2L;
        RecordEntity recordEntity = new RecordEntity();
        Record record = new Record();

        when(recordJPARepository.findByIdAndUserId(recordId, userId)).thenReturn(Optional.of(recordEntity));
        when(mapper.map(recordEntity, Record.class)).thenReturn(record);

        Record result = recordRepository.findByIdAndUserId(recordId, userId);

        assertNotNull(result);
        assertEquals(result, record);
    }

    @Test
    void findByIdAndUserId_NotFound() {
        Long recordId = 1L;
        Long userId = 2L;

        when(recordJPARepository.findByIdAndUserId(recordId, userId)).thenReturn(Optional.empty());

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> recordRepository.findByIdAndUserId(recordId, userId));
        assertEquals("Unable to find the record: " + recordId, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void findAllByUserIdAndInactive_Success() {
        String search = "test";
        Pageable pageable = Pageable.unpaged();
        Long userId = 1L;
        Boolean inactive = false;
        RecordEntity recordEntity = new RecordEntity();
        Record record = new Record();
        Page<RecordEntity> page = new PageImpl<>(List.of(recordEntity));

        when(recordJPARepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.map(recordEntity, Record.class)).thenReturn(record);

        Page<Record> result = recordRepository.findAllByUserIdAndInactive(search, pageable, userId, inactive);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(recordJPARepository).findAll(any(Specification.class), eq(pageable));
        verify(mapper).map(recordEntity, Record.class);
    }

    @Test
    void findAllByUserIdAndInactive_Exception() {
        String search = "test";
        Pageable pageable = Pageable.unpaged();
        Long userId = 1L;
        Boolean inactive = false;

        when(recordJPARepository.findAll(any(Specification.class), eq(pageable))).thenThrow(new RuntimeException("Database error"));

        ExceptionResponse exception = assertThrows(ExceptionResponse.class, () -> recordRepository.findAllByUserIdAndInactive(search, pageable, userId, inactive));
        assertEquals("Unable to search for records", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}