package com.fileoperations.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fileoperations.dto.FileDto;

public interface FileService {

	FileDto saveNewFile(MultipartFile file);
	List<FileDto> getAllFiles();
	FileDto getOneDto(String Id);
	Boolean deleteFile(String name);
	String getFile(String name);
}
