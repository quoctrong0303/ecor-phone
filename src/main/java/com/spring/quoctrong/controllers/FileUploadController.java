package com.spring.quoctrong.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.quoctrong.models.FileUploadedLocation;
import com.spring.quoctrong.services.FilesStorageService;

//Xử lý upload hình ảnh, tệp tin,..
@Controller
@RequestMapping("/file")
@CrossOrigin("*")
public class FileUploadController {

	
	@Autowired
	FilesStorageService filesStorageService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(value = "/uploads", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<FileUploadedLocation> upload(@RequestPart(required = false, value = "file") MultipartFile file) {
		String url;
		if (file != null && !file.isEmpty()) {
			try {

				String generatedFileName = filesStorageService.save(file);
				url = "file/" + generatedFileName; 
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(new FileUploadedLocation(url));
	}
	
//	Xem file trên browser
	// done
	@GetMapping("/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<byte[]> readFile(@PathVariable String fileName) {
		try {
			byte[] bytes = filesStorageService.load(fileName);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	
	
	
}
