package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.model.user.Wallet;
import com.calculator.manager.domain.repository.IOperationRepository;
import com.calculator.manager.domain.service.IOperationService;
import com.calculator.manager.domain.service.IRecordService;
import com.calculator.manager.domain.service.IUserService;
import com.calculator.manager.domain.service.IWalletService;
import com.calculator.manager.domain.validation.IOperationValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class that implement interface and realize actions about operations.
 */
@Service
public class OperationServiceImpl implements IOperationService {

    private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);

    @Autowired
    private IOperationRepository operationRepository;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOperationValidation operationValidation;

    /**
     * Method to check if the operation can be realized
     * This method call validation method, recover models that will user
     * and call method the realize operation.
     * This method is generic for any operation.
     * @param userId
     * @param operationFields
     * @return String with response of operation
     */
    @Override
    public String operation(Long userId, OperationFields operationFields) {
        logger.info("Realize validation to operation: {}", operationFields.getType());
        operationValidation.validate(operationFields);

        logger.info("Get the operation by type: {}", operationFields.getType());
        Operation operation = operationRepository.findByType(operationFields.getType());

        logger.info("Get the wallet from user: {}", userId);
        Wallet wallet = walletService.getByUserId(userId);

        logger.info("Get user: {}", userId);
        User user = userService.getById(userId);

        try {
            return callOperation(wallet, user, operation, operationFields);
        } catch (ExceptionResponse ex) {
            logger.error("Error to realize operation");
            throw ex;
        }
    }

    /**
     * Transactional method to check if user has balance and update registry.
     * If some exception happens the transaction annotation do rollback.
     * The method performsOperation is called based on the type of operation,
     * depends on the operation call the class and method to realize it.
     *
     * If all things run call the method to update user's wallet and to save the new record
     */
    @Transactional
    private String callOperation(Wallet wallet, User user, Operation operation, OperationFields operationFields) {
        Double newBalance = wallet.getBalance() - operation.getCost();
        logger.info("Check if user {} has balance to realize operation: {}", user.getId(), operation.getType());
        if (newBalance < 0) {
            throw new ExceptionResponse("Insufficient balance for this operation", HttpStatus.FORBIDDEN);
        }

        logger.info("Call method to realize operation {} with number1 {} and number2 {}",
                operation.getType(), operationFields.getNumber1(), operationFields.getNumber2());

        String operationResult = operationFields.getType().operationAction
                .performsOperation(operationFields.getNumber1(), operationFields.getNumber2());

        logger.info("Update new balance to user: {}", user.getId());
        walletService.updateBalance(wallet, newBalance);

        logger.info("Create a new record to user: {}", user.getId());
        recordService.createRecord(operation, user, newBalance, operationResult);
        return operationResult;
    }

    /**
     * Get every operation on database.
     */
    @Override
    public List<Operation> getOperations() {
        return operationRepository.findAll();
    }
}
