package com.fileoperations.dto;

public class FileDto{

	private String filePath;
	private Long fileSize;
	private String fileName;
	private String fileExtension;
	
	public FileDto() {
	}

	public FileDto(Long fileSize, String fileName, String fileExtension) {
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Override
	public String toString() {
		return "FileDto [filePath=" + filePath + ", fileSize=" + fileSize + ", fileName=" + fileName
				+ ", fileExtension=" + fileExtension + "]";
	}
	
	
	
	
}
