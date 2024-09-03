package com.calculator.manager.domain.utils;

import com.calculator.manager.domain.service.impl.operation.action.OperationActionSquareRootImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @InjectMocks
    private Utils utils;


    @Test
    void getCurrencyValue() {
        String result = Utils.getCurrencyValue(11.1803398875);
        assertEquals(result, "$11.18");
    }
}