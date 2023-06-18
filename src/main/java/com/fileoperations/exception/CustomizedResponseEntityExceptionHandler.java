package com.fileoperations.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fileoperations.exception.customException.BadRequestException;
import com.fileoperations.exception.customException.NotFoundException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleVehicleNotFoundException(NotFoundException ex, WebRequest request){
		ApiError apiError = new ApiError(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
	
		return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request){
		ApiError apiError = new ApiError(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
	
		return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
	}
	
}
