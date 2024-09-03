package com.calculator.manager.domain.service.impl.operation.action;

import com.calculator.manager.domain.gateway.IRandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationActionRandomStringImplTest {

    @InjectMocks
    private OperationActionRandomStringImpl operationActionRandomStringImpl;

    @Mock
    private IRandomString randomString;

    @Test
    void performsOperationRandomString() {
        when(randomString.generateRandomString()).thenReturn("JHYTGFATELKJTUF");

        assertEquals(operationActionRandomStringImpl.performsOperation(null, null), "JHYTGFATELKJTUF");
    }
}