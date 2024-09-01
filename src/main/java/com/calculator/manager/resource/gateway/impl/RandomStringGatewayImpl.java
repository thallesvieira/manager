package com.calculator.manager.resource.gateway.impl;

import com.calculator.manager.domain.exception.ExceptionResponse;
import com.calculator.manager.domain.gateway.IRandomString;
import com.calculator.manager.resource.gateway.IRandomStringFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation from gateway that user interface feign to generate random string
 */
@Service
public class RandomStringGatewayImpl implements IRandomString {
    private static final Logger logger = LoggerFactory.getLogger(RandomStringGatewayImpl.class);

    @Autowired
    private IRandomStringFeign randomStringFeign;

    /**
     * Call feign method that generate a random string
     * @return
     */
    @Override
    public String generateRandomString() {
        try {
            logger.info("Trying generate a random string");
            return randomStringFeign.getRandomStrings(
                    1,
                    16,
                    "off",
                    "on",
                    "off",
                    "on",
                    "plain",
                    "new").trim();
        } catch (Exception ex) {
            logger.error("Error to generate a random string");
            throw new ExceptionResponse("Unable to generate a random string", HttpStatus.BAD_REQUEST);
        }
    }
}
