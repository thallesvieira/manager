package com.calculator.manager.domain.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Class to configure the throws' exception.
 */
@ControllerAdvice
public class ExceptionResponseControllerAdvice extends ResponseEntityExceptionHandler {

	/**
	 * This method is responsible to receive the exception and throw it.
	 * This configuration do the controller throws the exception that the service throws.
	 */
	@ExceptionHandler(ExceptionResponse.class)
	public final ResponseEntity<ExceptionResponseCustom> internalException(ExceptionResponse ex, HttpServletRequest request) {
		
		ExceptionResponseCustom errorHandling = new ExceptionResponseCustom();
		
		errorHandling.setTimestamp(LocalDateTime.now());
		errorHandling.setStatus(ex.getStatus());
		errorHandling.setCode(ex.getStatus().value());
		errorHandling.setMessage(ex.getMessage());
		errorHandling.setPath(request.getRequestURI());
		
		return new ResponseEntity<>(errorHandling, ex.getStatus());
	
	}
}
