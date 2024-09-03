package com.calculator.manager.application.web.controller;

import com.calculator.manager.application.web.dto.response.OperationDTOResponse;
import com.calculator.manager.domain.model.OperationFields;
import com.calculator.manager.domain.service.IOperationService;
import com.calculator.manager.domain.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to realize operation
 */
@RestController
@RequestMapping("/operation")
public class OperationController {

    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private IOperationService operationService;

    @Autowired
	private HttpServletRequest request;

    @Autowired
    private ITokenService tokenService;

    /**
     * This method receive the request for save a new operation
     * @param operationFields
     * @return
     */
    @PostMapping("/realize")
    public ResponseEntity<?> saveOperation(@RequestBody OperationFields operationFields) {
        try {
            Long userId = tokenService.getUserId(tokenService.retrieve(request));
            logger.info("User trying  to realize a new operation");
            return ResponseEntity.ok(operationService.realizeOperation(userId, operationFields));
        } catch (Exception ex) {
            logger.error("Error to realize a new operation");
            throw ex;
        }
    }

    /**
     * Method to get all operations and return with name and cost
     * @return
     */
    @GetMapping("/operations")
    public ResponseEntity<?> getOperations() {
        try {
            logger.info("Trying get the list of operations");
            List<OperationDTOResponse> operationDTOResponseList = operationService.getOperations().stream()
                    .map(op->new OperationDTOResponse().toDTO(op)).collect(Collectors.toList());
            return ResponseEntity.ok(operationDTOResponseList);
        } catch (Exception ex) {
            logger.info("Error to get the list of operations");
            throw ex;
        }
    }
}
