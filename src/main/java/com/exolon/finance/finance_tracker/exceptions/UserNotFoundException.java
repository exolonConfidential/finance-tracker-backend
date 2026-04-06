package com.exolon.finance.finance_tracker.exceptions;

public class UserNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	public UserNotFoundException(String message){
		super(message);
	}
}
