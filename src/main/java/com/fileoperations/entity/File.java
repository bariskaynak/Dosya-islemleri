package com.fileoperations.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class File extends BaseEntity{

	@Column(name = "name")
	private String fileName;
	@Column(name = "path")
	private String filePath;
	@Column(name = "size")
	private Long fileSize;
	@Column(name = "extension")
	private String fileExtension;
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
	
	public File() {
	}
	
	public File(String fileName, String filePath, Long fileSize, String fileExtension) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.fileExtension = fileExtension;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}
