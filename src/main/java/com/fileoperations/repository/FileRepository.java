package com.fileoperations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fileoperations.entity.File;

public interface FileRepository extends JpaRepository<File, String>{

	File findByFileName(String name);
}
