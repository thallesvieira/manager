package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.repository.IRecordRepository;
import com.calculator.manager.domain.service.IRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Class responsible for actions about records
 */
@Service
public class RecordServiceImpl implements IRecordService {
    private static final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    @Autowired
    private IRecordRepository recordRepository;

    /**
     * Create a new record after realize an operation.
     * Get user, operation, new balance from user and response to create it.
     * @param operation
     * @param user
     * @param newBalance
     * @param operationResponse
     * @return
     */
    @Override
    @Transactional
    public Record createRecord(Operation operation, User user, Double newBalance, String operationResponse) {
        Record record = new Record();
        record.setOperation(operation);
        record.setUser(user);
        record.setAmount(operation.getCost());
        record.setUserBalance(newBalance);
        record.setOperationResponse(operationResponse);
        record.setDate(LocalDateTime.now());
        record.setInactive(false);

        return recordRepository.save(record);
    }

    /**
     * Update field inactive to specific record.
     * This is a control to identify records that user "deleted"
     * @param inactive
     * @param userId
     * @param recordId
     * @return
     */
    @Override
    public Record inativateRecord(Boolean inactive, Long userId, Long recordId) {
        logger.info("Get record: {} for user: {}", recordId, userId);
        Record record = recordRepository.findByIdAndUserId(recordId, userId);

        logger.info("Update field inactive to: {}", inactive);
        record.setInactive(inactive);
        return recordRepository.save(record);
    }

    /**
     * Get all records from user and return it with pagination
     *
     * @param search - if as value find anything with this value
     * @param page - current page
     * @param pageSize - size of pages
     * @param userId - user that request it
     * @param inactive - filter to get actives or inactives records
     * @return
     */
    @Override
    public Page<Record> getRecords(String search, int page, int pageSize, Long userId, Boolean inactive) {
        logger.info("create a page with current page {}, page size {} and sort DESC from date",
                page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"));

        logger.info("Get all records for user: {} with search {} and that have field inactive = {}",
                    userId, search, inactive);
        return recordRepository.findAllByUserIdAndInactive(search, pageable, userId, inactive);
    }
}
