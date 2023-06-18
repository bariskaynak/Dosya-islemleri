package com.fileoperations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@SpringBootApplication
@OpenAPIDefinition(info = 
@io.swagger.v3.oas.annotations.info.Info(title = "File Operations Application", version = "1.0", description = "Documentation for File Operations"))
public class FileOperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileOperationsApplication.class, args);
	}

}
