package com.calculator.manager.domain.service;

import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.user.User;
import org.springframework.data.domain.Page;

public interface IRecordService {
    Record createRecord(Operation operation, User user, Double newBalance, String operationResponse);
    Record inativateRecord(Boolean inactive, Long userId, Long recordId);
    Page<Record> getRecords(String search, int page, int pageSize, Long userId, Boolean inactive);
}
