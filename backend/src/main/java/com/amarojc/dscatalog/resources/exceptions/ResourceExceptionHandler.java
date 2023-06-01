package com.amarojc.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;


@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(
				ObjectNotFoundException e,HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Resource not found");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(DatabaseIntegrityViolationException.class)
	public ResponseEntity<StandardError> databaseIntegrityViolation(
			DatabaseIntegrityViolationException dex, HttpServletRequest request){
		
			HttpStatus status = HttpStatus.BAD_REQUEST;
			StandardError error = new StandardError();
			error.setTimestamp(Instant.now());
			error.setStatus(status.value());
			error.setError("Database Integrity violation");
			error.setMessage(dex.getMessage());
			error.setPath(request.getRequestURI());
			
			return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(
			MethodArgumentNotValidException ex, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError error = new ValidationError();
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Validation exception");
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		for(FieldError field : ex.getBindingResult().getFieldErrors()) {
			error.addError(field.getField(), field.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(error);
		
		
	}
	
}
