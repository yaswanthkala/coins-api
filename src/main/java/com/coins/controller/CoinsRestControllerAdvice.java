package com.coins.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.coins.AppConstants;
import com.coins.exception.PossibilitiesNotFoundException;

@ControllerAdvice
public class CoinsRestControllerAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(PossibilitiesNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(PossibilitiesNotFoundException exception) {
		return new ResponseEntity<String>(AppConstants.INVALID_POSSIBILITIES, HttpStatus.BAD_REQUEST);
	}

}
