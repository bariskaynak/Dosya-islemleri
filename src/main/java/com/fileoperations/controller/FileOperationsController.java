package com.fileoperations.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.fileoperations.dto.FileDto;
import com.fileoperations.exception.customException.FileExtensionException;
import com.fileoperations.service.Impl.FileServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/file")
public class FileOperationsController {
	
	private final FileServiceImpl fileServiceImpl;
	
	public FileOperationsController(FileServiceImpl fileServiceImpl) {
		this.fileServiceImpl = fileServiceImpl;
	}

	@Operation(summary = "Save new file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File Recieved Succesfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@PostMapping("/uploadFile")
	public ResponseEntity<FileDto> hanleFileUpload(@RequestParam("file") MultipartFile file){
		HttpStatus status = HttpStatus.OK;
		FileDto newFileDto = null;
		
			newFileDto = fileServiceImpl.saveNewFile(file);
			
	
		

		return new ResponseEntity<>(newFileDto,status);
	}
	
	@Operation(summary = "Get all file info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All File Info Recieved Succesfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@GetMapping("/getAllFileInfo")
	public ResponseEntity<List<FileDto>> getAllFileInfo(){
		
		List<FileDto> allFileInfo = fileServiceImpl.getAllFiles();
		return allFileInfo.size() > 0 ? new ResponseEntity<>(allFileInfo,HttpStatus.OK) : new ResponseEntity<>(allFileInfo,HttpStatus.NO_CONTENT);
	}
	
	@Operation(summary = "Get file Info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File Info Recieved Succesfully"),
			@ApiResponse(responseCode = "204", description = "File Content Not Exist"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@GetMapping("/getFileInfo/{id}")
	public ResponseEntity<FileDto> getFileInfo(@PathVariable String id){
		
		FileDto fileInfo = fileServiceImpl.getOneDto(id);
		return new ResponseEntity<>(fileInfo,HttpStatus.OK);
	}
	
	@Operation(summary = "Delete file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File Deleted"),
			@ApiResponse(responseCode = "204", description = "File Content Not Found"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@DeleteMapping("/delete/{filename}")
	  public ResponseEntity<String> delete(@PathVariable String filename) {
	    
	    try {
	      boolean existed = fileServiceImpl.deleteFile(filename);
	      
	      if (existed) 
	        return ResponseEntity.status(HttpStatus.OK).body("Delete the file successfully: " + filename);
	      
	      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The file does not exist!");
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete the file: " + filename + ". Error: " + e.getMessage());
	    }
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<String> maxUploadSizeExceeded(MaxUploadSizeExceededException e) {
	    return ResponseEntity.badRequest().body("File Too Large");
	}
	
	@ExceptionHandler(FileExtensionException.class)
	public ResponseEntity<String> fileExtensionException(FileExtensionException e) {
	    return ResponseEntity.badRequest().body("File extension is not correct");
	}
}
