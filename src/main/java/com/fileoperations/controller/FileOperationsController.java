package com.fileoperations.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.fileoperations.Util.FileExtentions;
import com.fileoperations.dto.FileDto;
import com.fileoperations.exception.customException.FileExtensionException;
import com.fileoperations.security.JwtTokenProvider;
import com.fileoperations.service.Impl.FileServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/file")
public class FileOperationsController {
	
	private final FileServiceImpl fileServiceImpl;
	private final JwtTokenProvider jwtTokenProvider;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileOperationsController.class);
	
	public FileOperationsController(FileServiceImpl fileServiceImpl, JwtTokenProvider jwtTokenProvider) {
		this.fileServiceImpl = fileServiceImpl;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Operation(summary = "Save new file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File Recieved Succesfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@PostMapping("/uploadFile")
	public ResponseEntity<FileDto> hanleFileUpload(@RequestParam("file") MultipartFile file, @RequestHeader (name="Authorization") String token){
		String user = jwtTokenProvider.getUsernameFromJWT(token.split(" ")[1]);
		LOGGER.info(user + " accessed to getAllFileInfo api");
		FileDto newFileDto = fileServiceImpl.saveNewFile(file);;
		
		return new ResponseEntity<>(newFileDto,HttpStatus.OK);
	}
	
	@Operation(summary = "Get all file info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All File Info Recieved Succesfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
	})
	@GetMapping("/getAllFileInfo")
	public ResponseEntity<List<FileDto>> getAllFileInfo(@RequestHeader (name="Authorization") String token){
		String user = jwtTokenProvider.getUsernameFromJWT(token.split(" ")[1]);
		LOGGER.info(user + " accessed to getAllFileInfo api");
		
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
	public ResponseEntity<FileDto> getFileInfo(@PathVariable String id, @RequestHeader (name="Authorization") String token){
		String user = jwtTokenProvider.getUsernameFromJWT(token.split(" ")[1]);
		LOGGER.info(user + " accessed to getAllFileInfo api");
		
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
	public ResponseEntity<String> delete(@PathVariable String filename, @RequestHeader (name="Authorization") String token) {
		String user = jwtTokenProvider.getUsernameFromJWT(token.split(" ")[1]);
		LOGGER.info(user + " accessed to getAllFileInfo api");
		
	    try {
	      boolean existed = fileServiceImpl.deleteFile(filename);
	      
	      if (existed) 
	        return ResponseEntity.status(HttpStatus.OK).body("Delete the file successfully: " + filename);
	      
	      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The file does not exist!");
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete the file: " + filename + ". Error: " + e.getMessage());
	    }
	}
	
	
	@GetMapping(value = "/getfile/{filename}")
	public @ResponseBody ResponseEntity<byte[]> getImageWithMediaType(@PathVariable String filename, @RequestHeader (name="Authorization") String token) throws IOException {
		String user = jwtTokenProvider.getUsernameFromJWT(token.split(" ")[1]);
		LOGGER.info(user + " accessed to getAllFileInfo api");
		
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = FileExtentions.getContentType(filename);
	    headers.setContentType(mediaType);
	    
		byte[] fileContent = fileServiceImpl.getFile(filename);

	    return fileContent == null ?  new ResponseEntity<byte[]>(null, headers, HttpStatus.BAD_REQUEST): new ResponseEntity<byte[]>(fileContent, headers, HttpStatus.OK);
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
