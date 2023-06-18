package com.fileoperations.Util;

import java.util.HashSet;
import java.util.Set;

public class FileExtentions {

	private static Set<String> extentions = new HashSet<String>() {
		{
			add("png");
			add("jpeg");
			add("jpg");
			add("docx");
			add("pdf");
			add("xlsx");
		}
	};
	
	public static boolean isExtension(String extention) {
		return extentions.contains(extention);
	}
}
