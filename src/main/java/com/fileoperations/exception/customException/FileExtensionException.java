package com.fileoperations.exception.customException;

public class FileExtensionException extends RuntimeException{

	public FileExtensionException(String message) {
		super(message);
	}
	
	public FileExtensionException(String message, Throwable ex) {
		super(message,ex);
	}
	
}
