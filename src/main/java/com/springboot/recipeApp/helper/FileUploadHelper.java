package com.springboot.recipeApp.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	
	
	public final String UPLOAD_DIR = new ClassPathResource("static/image/").getFile().getAbsolutePath();
	
	
	public FileUploadHelper() throws IOException{
		
	}
	
	public boolean uploadFile(MultipartFile multiFile) {
		
		boolean res = false;
		try {
			
			Files.copy(multiFile.getInputStream(),Paths.get(UPLOAD_DIR+File.separator+multiFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
			res=true;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
