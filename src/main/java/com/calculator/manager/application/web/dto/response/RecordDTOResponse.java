package com.calculator.manager.application.web.dto.response;

import com.calculator.manager.domain.model.Record;
import com.calculator.manager.domain.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RecordDTOResponse {
    Long id;
    String operation;
    String amount;
    String balance;
    String response;
    String date;
    Boolean inactive;

    public static RecordDTOResponse toRecordDTOResponse(Record record) {
        RecordDTOResponse recordDTOResponse = new RecordDTOResponse();
        recordDTOResponse.setId(record.getId());
        recordDTOResponse.setOperation(record.getOperation().getType().getName());
        recordDTOResponse.setAmount(Utils.getCurrencyValue(record.getAmount()));
        recordDTOResponse.setBalance(Utils.getCurrencyValue(record.getUserBalance()));
        recordDTOResponse.setResponse(record.getOperationResponse());
        recordDTOResponse.setDate(record.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")));
        recordDTOResponse.setInactive(record.getInactive());

        return recordDTOResponse;
    }
}
