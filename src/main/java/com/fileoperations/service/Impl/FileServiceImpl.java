package com.fileoperations.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fileoperations.Util.FileExtentions;
import com.fileoperations.controller.FileOperationsController;
import com.fileoperations.dto.FileDto;
import com.fileoperations.entity.File;
import com.fileoperations.exception.customException.FileExtensionException;
import com.fileoperations.exception.customException.FileLocationException;
import com.fileoperations.exception.customException.NotFoundException;
import com.fileoperations.repository.FileRepository;
import com.fileoperations.service.FileService;

/**
 * 
 * @author bkaynak
 * @version 1.0
 */
@Service
public class FileServiceImpl implements FileService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FileOperationsController.class);

	private final Path fileLocationPath;
	private final FileRepository fileRepository;
	private final ModelMapper mapper;
	
	/**
	 * 
	 * @param filePath for file path
	 * @param fileRepository database operation
	 * @param mapper dto-model and model-dto transformation
	 */
	
	public FileServiceImpl(@Value("${file.path}") String filePath, FileRepository fileRepository, ModelMapper mapper) {
		this.fileLocationPath = Paths.get(filePath).toAbsolutePath().normalize();
		this.fileRepository = fileRepository;
		this.mapper = mapper;
		
		 try {
	            Files.createDirectories(this.fileLocationPath);
	        } catch (Exception ex) {
	            throw new FileLocationException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	}

	/**
	 * @param file create new file contents
	 * @return FileDto
	 */
	@Override
	public FileDto saveNewFile(MultipartFile file) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String ext = FilenameUtils.getExtension(fileName);
		
		if(!FileExtentions.isExtension(ext))
			throw new FileExtensionException("File extension is not correct ");
		
		Path targetLocation = this.fileLocationPath.resolve(fileName);
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			File newFile = new File(fileName, this.fileLocationPath.toString(),file.getSize(),ext);
			return this.mapper.map(fileRepository.save(newFile), FileDto.class);
			
		} catch (Exception e) {
			throw new FileLocationException("Could not store fileName : " + fileName);
		}
		
	}

	/**
	 * @return all file contents
	 */
	@Override
	public List<FileDto> getAllFiles() {
		List<File> files = fileRepository.findAll();
		List<FileDto> dtos = files
				  .stream()
				  .map(file -> this.mapper.map(file, FileDto.class))
				  .collect(Collectors.toList());
		return dtos;
	}

	/**
	 * @param Id file id
	 * @return one file
	 */
	@Override
	public FileDto getOneDto(String id) {
		File file = fileRepository.findById(id).orElseThrow(() -> new NotFoundException("File Does not exist"));
		return this.mapper.map(file, FileDto.class);
	}

	/**
	 * @param name deleted filename
	 * @return if delete true or false
	 */
	@Override
	public Boolean deleteFile(String name) {
		 try {
			  File deletedFile = fileRepository.findByFileName(name);
			  deletedFile.setIsDeleted(true);
			  fileRepository.save(deletedFile);
		      Path file = fileLocationPath.resolve(name);
		      return Files.deleteIfExists(file);
		    } catch (IOException e) {
		      throw new RuntimeException("Error: " + e.getMessage());
		    }
	}

	/**
	 * @param name filename
	 * @return filename path
	 */
	@Override
	public byte[] getFile(String name) {
		 try {
			String path = fileLocationPath.resolve(name).toString();
			java.io.File file = new java.io.File(path);
			byte[] bytes = FileUtils.readFileToByteArray(file);
			return bytes;
		} catch (IOException e) {
			LOGGER.error("File Not Found => " + name);
		}
		
		return null;
	}

}
