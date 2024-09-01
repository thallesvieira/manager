package com.calculator.manager.domain.exception;

import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponseCustom {
	private LocalDateTime timestamp;
	private HttpStatus status;
	private int code;
	private String message;
	private String path;
}
