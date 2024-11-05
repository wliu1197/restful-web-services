package com.rest.webservices.restful_web_services.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	//handler UserNotFoundException in our error dataDetails format
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<CustomErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception {
		
		CustomErrorDetails errorDetails = new CustomErrorDetails(ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	//handler all other exceptions in our error dataDetails format
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorDetails> handleAllOtherException(Exception ex, WebRequest request) throws Exception {
		
		CustomErrorDetails errorDetails = new CustomErrorDetails(ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//by default validation exceptions doesn't should failed message
	//this is to customize validation exceptions will show failed message in our error dataDetail format
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		CustomErrorDetails errorDetails = new CustomErrorDetails(ex.getFieldError().getDefaultMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetails,HttpStatus.BAD_REQUEST);
	}
}
