package com.calculator.manager.domain.model;

import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Record {
    Long id;
    Operation operation;
    User user;
    Double amount;
    Double userBalance;
    String operationResponse;
    LocalDateTime date;
    Boolean inactive;
}
