package com.calculator.manager.application.web.controller;

import com.calculator.manager.application.web.dto.response.RecordDTOResponse;
import com.calculator.manager.domain.service.IRecordService;
import com.calculator.manager.domain.service.ITokenService;
import com.calculator.manager.domain.service.IWalletService;
import com.calculator.manager.domain.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible to realize actions about user data
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ITokenService tokenService;

    /**
     * Get the current user's balance
     * @return
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getUserBalance() {
        try {
            Long userId = tokenService.getUserId(tokenService.retrieve(request));
            logger.info("Trying get current user's balance from user {}", userId);

            return ResponseEntity.ok(Utils.getCurrencyValue(walletService.getByUserId(userId).getBalance()));
        } catch (Exception ex) {
            logger.error("Error to get current user's balance ");
            throw ex;
        }
    }

    /**
     * Method responsible to get all records from user.
     *
     * @param search - Search anything that contains the text
     * @param page - current page
     * @param size - size of pages
     * @param inactive - find actives and inactives user
     * @return
     */
    @GetMapping("/records")
    public ResponseEntity<?> getUserRecords(@RequestParam(required = false) String search,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "false") Boolean inactive)  {
        try {
            Long userId = tokenService.getUserId(tokenService.retrieve(request));

            logger.info("Trying get user's records to user {}", userId);
            Page<RecordDTOResponse> records = recordService.getRecords(search, page, size, userId, inactive)
                    .map(RecordDTOResponse::toRecordDTOResponse);
            return ResponseEntity.ok(records);
        } catch (Exception ex) {
            logger.error("Error to get user's records ");
            throw ex;
        }
    }

    /**
     * Method to inactivate or activate a record.
     * With this is possible to delete and recover the record
     * @param recordId
     * @param inactive
     * @return
     */
    @PutMapping("/record/{recordId}/inactivate")
    public ResponseEntity<?> inactivateUserRecords(@PathVariable Long recordId,
                                                   @RequestParam(required = false) Boolean inactive
    )  {
        try {
            Long userId = tokenService.getUserId(tokenService.retrieve(request));
            logger.info("Trying to update field inactive from user {} to {} ", userId, inactive);
            return ResponseEntity.ok(
                    RecordDTOResponse.toRecordDTOResponse(recordService.inativateRecord(inactive, userId, recordId)));
        } catch (Exception ex) {
            logger.error("Error to update field inactive from user");
            throw ex;
        }
    }
}
