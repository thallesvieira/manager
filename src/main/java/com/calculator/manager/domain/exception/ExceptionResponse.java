package com.calculator.manager.domain.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Class default to throws an exception.
 * Only call this class with a message and status:
 * throw new ExceptionResponse("Message...", HttpStatus.BAD_REQUEST);
 */
public class ExceptionResponse extends RuntimeException {
	
	private static final long serialVersionUID = 7635744926677754658L;
	
	@Getter
	private final HttpStatus status;
	@Getter
	private final int code;
	@Getter
	private final String message;
	
	public ExceptionResponse(String message) {
		super(message);
		this.status = null;
		this.message = "";
		this.code = 0;
    }
	
	public ExceptionResponse(String errorMessage, HttpStatus status) {
		super(errorMessage);
		
		this.code = status.value();
		this.message = errorMessage;
		this.status = status;
	}
}
