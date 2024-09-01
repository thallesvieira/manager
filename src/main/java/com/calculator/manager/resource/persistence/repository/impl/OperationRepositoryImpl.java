package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.repository.IOperationRepository;
import com.calculator.manager.resource.persistence.repository.IOperationJPARepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Operation repository to search OperationEntity and save it
 */
@Repository
public class OperationRepositoryImpl implements IOperationRepository {
    private static final Logger logger = LoggerFactory.getLogger(OperationRepositoryImpl.class);

    @Autowired
    private IOperationJPARepository operationRepository;

    private ModelMapper mapper = new ModelMapper();

    /**
     * Find operation by type and convert for model to return to domain package.
     * @param type
     * @return
     */
    @Override
    public Operation findByType(OperationType type) {
        logger.info("Getting operation by type: {}", type);
        return operationRepository.findByType(type)
                .map(op -> mapper.map(op, Operation.class))
                .orElseThrow(() -> new ExceptionResponse("Operation not found!", HttpStatus.NOT_FOUND));
    }

    /**
     * Find all operation in database, convert for model and return to service
     * @return
     */
    @Override
    public List<Operation> findAll() {
        try {
            logger.info("Getting all operations");
            return operationRepository.findAll().stream().map(entity ->  mapper.map(entity, Operation.class))
                    .collect(Collectors.toList());
        }catch (ExceptionResponse ex) {
            logger.error("Error to get all operations");
            throw new ExceptionResponse("Unable to load operations!", HttpStatus.BAD_REQUEST);
        }
    }
}
