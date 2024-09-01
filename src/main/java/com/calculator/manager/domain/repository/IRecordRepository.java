package com.calculator.manager.domain.repository;

import com.calculator.manager.domain.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecordRepository {
    Record save(Record record);
    Record findByIdAndUserId(Long recordId, Long userId);
    Page<Record> findAllByUserIdAndInactive(String search, Pageable pageable, Long userId, Boolean inactive);
}
