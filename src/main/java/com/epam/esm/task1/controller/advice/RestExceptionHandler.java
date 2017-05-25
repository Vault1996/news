package com.epam.esm.task1.controller.advice;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.epam.esm.task1.dto.ExceptionContainer;
import com.epam.esm.task1.exception.ValidationException;
import com.epam.esm.task1.repository.exception.EntityNotFoundException;
import com.epam.esm.task1.repository.exception.UniqueConstraintViolationException;

/**
 * Global handler of all rest services
 * 
 * @author Algis_Ivashkiavichus
 *
 */
@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger LOGGER = LogManager.getLogger();

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ExceptionContainer> validationExceptionHandler(Exception ex) {
		LOGGER.error("Validation exception occurs", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
		return response;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionContainer> illegalArgumentExceptionHandler(Exception ex) {
		LOGGER.error("Illegal argument exception occurs", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.BAD_REQUEST.value(), "Illegal argument");
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
		return response;
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionContainer> entityNotFoundException(Exception ex) {
		LOGGER.error("Entity not found", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
		return response;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionContainer> notReadableException(Exception ex) {
		LOGGER.error("Request body is not readable", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.BAD_REQUEST.value(),
				"Request body is not readable");
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
		return response;
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ExceptionContainer> mediaTypeNotSupportedException(Exception ex) {
		LOGGER.error("Media type is not supported", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
				ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception,
				HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return response;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionContainer> httpRequestMethodNotSupportedException(Exception ex) {
		LOGGER.error("Request method is not supported", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.METHOD_NOT_ALLOWED);
		return response;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ExceptionContainer> noHandlerFoundException(Exception ex) {
		LOGGER.error("No handler found", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
		return response;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ExceptionContainer>> processValidationError(MethodArgumentNotValidException ex) {
		LOGGER.error("Validation exception", ex);
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}

	@ExceptionHandler(UniqueConstraintViolationException.class)
	public ResponseEntity<ExceptionContainer> uniqueConstraintViolationException(Exception ex) {
		LOGGER.error("Unique constraint violation", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.CONFLICT.value(), ex.getMessage());
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.CONFLICT);
		return response;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionContainer> integrityViolationException(Exception ex) {
		LOGGER.error("Something went wrong. Please, check your data and try again.", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Something went wrong. Please, check your data and try again.");
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		return response;
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ExceptionContainer> unknownException(Exception ex) {
		LOGGER.error("Unknown exception", ex);
		ExceptionContainer exception = new ExceptionContainer(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal server error");
		ResponseEntity<ExceptionContainer> response = new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		return response;
	}

	private ResponseEntity<List<ExceptionContainer>> processFieldErrors(List<FieldError> fieldErrors) {

		List<ExceptionContainer> exceptionList = new ArrayList<>();
		for (FieldError fieldError : fieldErrors) {
			ExceptionContainer exceptionContainer = new ExceptionContainer(HttpStatus.BAD_REQUEST.value(),
					fieldError.getDefaultMessage());
			exceptionList.add(exceptionContainer);
		}
		return new ResponseEntity<>(exceptionList, HttpStatus.BAD_REQUEST);
	}
}
