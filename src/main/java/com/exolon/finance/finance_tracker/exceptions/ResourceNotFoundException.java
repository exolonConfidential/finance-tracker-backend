package com.exolon.finance.finance_tracker.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
