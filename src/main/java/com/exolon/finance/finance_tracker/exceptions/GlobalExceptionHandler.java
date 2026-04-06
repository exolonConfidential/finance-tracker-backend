package com.exolon.finance.finance_tracker.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass()) ;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(UserNotFoundException ex, HttpServletRequest req) {
       
        ApiError error  = ApiError.builder()
				.status(404)
				.message(ex.getMessage())
				.details(req.getRequestURI())
				.timestamp(Instant.now())
				.build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldErrorDto> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> new FieldErrorDto(e.getField(), e.getRejectedValue(), e.getDefaultMessage()))
            .collect(Collectors.toList());

        ApiError error = new ApiError(
            400,
            "Validation failed",
            req.getRequestURI(),
            Instant.now(),
            fieldErrors
        );

        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleItemAlreadyExists(ItemAlreadyExistsException ex, HttpServletRequest req){
    		
    		ApiError error  = ApiError.builder()
    				.status(409)
    				.message(ex.getMessage())
    				.details(req.getRequestURI())
    				.timestamp(Instant.now())
    				.build();
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ItemAlreadyExistsException ex, HttpServletRequest req){
   
    	ApiError error  = ApiError.builder()
				.status(401)
				.message(ex.getMessage())
				.details(req.getRequestURI())
				.timestamp(Instant.now())
				.build();
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleResourceConflict(ItemAlreadyExistsException ex, HttpServletRequest req){
    
	    	ApiError error  = ApiError.builder()
	    			.status(403)
	    			.message(ex.getMessage())
	    			.details(req.getRequestURI())
	    			.timestamp(Instant.now())
	    			.build();
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAnyOther(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        ApiError error  = ApiError.builder()
				.status(500)
				.message(ex.getMessage())
				.details(req.getRequestURI())
				.timestamp(Instant.now())
				.build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}