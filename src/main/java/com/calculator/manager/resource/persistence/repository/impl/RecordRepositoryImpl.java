package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.repository.IRecordRepository;
import com.calculator.manager.resource.persistence.entity.RecordEntity;
import com.calculator.manager.resource.persistence.repository.IOperationJPARepository;
import com.calculator.manager.resource.persistence.repository.IRecordJPARepository;
import com.calculator.manager.resource.persistence.repository.IUserJPARepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

/**
 * Repository to RecordEntity
 */
@Repository
public class RecordRepositoryImpl implements IRecordRepository {
    private static final Logger logger = LoggerFactory.getLogger(RecordRepositoryImpl.class);

    @Autowired
    private IRecordJPARepository recordJPARepository;

    @Autowired
    private IOperationJPARepository operationJPARepository;

    @Autowired
    private IUserJPARepository userJPARepository;

    private ModelMapper mapper = new ModelMapper();

    /**
     * Save or update record in database.
     * Receive the model convert to entity, search user entity and operation entity
     * to restore conction with these entities and save it.
     *
     * @param record
     * @return
     */
    @Override
    public Record save(Record record) {
        try {
            logger.info("Maps record to record entity");
            RecordEntity recordEntity = mapper.map(record, RecordEntity.class);

            logger.info("Get operation entity by operation id");
            recordEntity.setOperation(operationJPARepository.findById(recordEntity.getOperation().getId()).get());

            logger.info("Get user entity by user id");
            recordEntity.setUser(userJPARepository.findById(recordEntity.getUser().getId()).get());

            logger.info("Save record and maps to model");
            return mapper.map(recordJPARepository.save(recordEntity), Record.class);
        }catch (Exception ex) {
            logger.error("Error to save the record");
            throw new ExceptionResponse("Unable to save the record", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Find record by id and user id.
     *
     * @param recordId
     * @param userId
     * @return
     */
    @Override
    public Record findByIdAndUserId(Long recordId, Long userId) {
        logger.info("Get record entity by id {} and user id {}", recordId, userId);
        return recordJPARepository.findByIdAndUserId(recordId, userId)
                .map(entity-> mapper.map(entity, Record.class))
                .orElseThrow(()->
                        new ExceptionResponse("Unable to find the record: "+recordId, HttpStatus.NOT_FOUND));
    }

    /**
     * Get records from user based on filter
     *
     * @param search
     * @param pageable
     * @param userId
     * @param inactive
     * @return
     */
    @Override
    public Page<Record> findAllByUserIdAndInactive(String search, Pageable pageable, Long userId, Boolean inactive) {
        try {
            logger.info("Create a filter specification with filters search {}," +
                    "user id {} and field inactive = {}", search, userId, inactive);
            Specification<RecordEntity> spec = new RecordSpecification(search, userId, inactive);

            logger.info("Search all records with filter and convert to model");
            return recordJPARepository.findAll(spec, pageable)
                    .map(records -> mapper.map(records, Record.class));

        } catch (Exception ex) {
            logger.error("Error to search records");
            throw new ExceptionResponse("Unable to search for records", HttpStatus.BAD_REQUEST);
        }
    }
}
