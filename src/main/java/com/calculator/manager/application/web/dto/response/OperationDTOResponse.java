package com.calculator.manager.application.web.dto.response;

import com.calculator.manager.domain.model.operation.Operation;
import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.domain.utils.Utils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationDTOResponse {
    Long id;
    OperationType type;
    String name;
    String cost;

    public OperationDTOResponse toDTO(Operation operation) {
        OperationDTOResponse operationDto = new OperationDTOResponse();
        operationDto.setId(operation.getId());
        operationDto.setType(operation.getType());
        operationDto.setName(operation.getType().getName());
        operationDto.setCost(Utils.getCurrencyValue(operation.getCost()));

        return operationDto;
    }
}
