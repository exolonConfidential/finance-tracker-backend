package com.exolon.finance.finance_tracker.exceptions;

import java.io.Serial;

public class ResourceConflictException extends RuntimeException{

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 4L;
	
	public ResourceConflictException(String message){
		super(message);
	}
}
