package com.exolon.finance.finance_tracker.exceptions;

import java.time.Instant;
import java.util.List;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
	private int status;
	private String message;
	private String details;
	private Instant timestamp;
	@Nullable
	private List<FieldErrorDto> fieldErrors;

}
