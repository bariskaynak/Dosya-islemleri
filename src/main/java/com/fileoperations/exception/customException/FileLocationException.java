package com.fileoperations.exception.customException;


public class FileLocationException extends RuntimeException{

	public FileLocationException(String message) {
		super(message);
	}
	
	public FileLocationException(String message, Throwable ex) {
		super(message, ex);
	}
}
