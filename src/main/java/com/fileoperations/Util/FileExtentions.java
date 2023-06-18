package com.fileoperations.Util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;

public class FileExtentions {

	private static Set<String> extentions = new HashSet<String>() {
		{
			add("png");
			add("jpeg");
			add("jpg");
			add("pdf");
			add("docx");
			
			add("xlsx");
		}
	};
	
	public static boolean isExtension(String extention) {
		return extentions.contains(extention);
	}
	
	public static MediaType getContentType(String filename) {
		String ext = FilenameUtils.getExtension(filename);
		switch (ext) {
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpeg":
		case "jpg":
			return MediaType.IMAGE_JPEG;
		case "pdf":
			return MediaType.APPLICATION_PDF;
		case "xlsx":
			return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		case "docx":
			return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		default:
			throw new IllegalArgumentException("Unexpected value: " + ext);
		}

	}
}
