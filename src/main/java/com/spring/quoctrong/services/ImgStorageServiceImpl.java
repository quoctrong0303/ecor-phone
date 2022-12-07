package com.spring.quoctrong.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgStorageServiceImpl implements FilesStorageService{

	private final Path root = Paths.get("uploads");
	@Override
	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initalize folder for uploads");
		}
		
	}

	@Override
	public String save(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file.");
			}
//			Check file is image?
			if (!isImageFile(file)) {
				throw new RuntimeException("You can only load image file");
			}
			float fileSizeInMegabytes =file.getSize() / 1_000_000.0f;
			if (fileSizeInMegabytes > 5.0f) {
				throw new RuntimeException("File must be <= 5Mb");
			}
			
//			File must be rename
			String fileExtesion = FilenameUtils.getExtension(file.getOriginalFilename());
			String generatedFileName = UUID.randomUUID().toString().replace("-", "");
			generatedFileName = generatedFileName+"."+fileExtesion;
			Path destinationFilePath = this.root.resolve(Paths.get(generatedFileName))
					.normalize().toAbsolutePath();
			if (!destinationFilePath.getParent().equals(this.root.toAbsolutePath())) {
				throw new RuntimeException("Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			return generatedFileName;
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error:" + e.getMessage());
		}
		
	}

	@Override
	public byte[] load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists()|| resource.isReadable()) {
				byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
				return bytes;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error:" + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
		
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
	
	private boolean isImageFile(MultipartFile file) {
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		
		return Arrays.asList(new String[] {"png", "jpg", "bmp"})
				.contains(fileExtension.trim().toLowerCase());
	}

	
}
