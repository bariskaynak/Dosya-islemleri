package com.fileoperations.exception;

import java.time.LocalDateTime;


public class ApiError {
	private LocalDateTime exceptionTime;
	private String message;
	private String detail;
	
	public ApiError(LocalDateTime exceptionTime, String message, String detail) {
		this.exceptionTime = exceptionTime;
		this.message = message;
		this.detail = detail;
	}

	public LocalDateTime getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(LocalDateTime exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	
}
