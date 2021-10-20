package com.example.filehandling.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandlingService {

	@Value("${upload.path}")
	private String uploadPath;

//	private Path root = Paths.get(uploadPath);

	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(Paths.get(uploadPath));
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload");
		}
	}

	public void save(MultipartFile file) {
		try {
			Path copyLocation = Paths
					.get(uploadPath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	public Resource load(String fileName) {
		try {
			Path file = Paths.get(uploadPath).resolve(fileName);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file");
			}

		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
	}

	public List<Path> loadAll() {
		try {
			Path root = Paths.get(uploadPath);
			return Files.walk(root, 1).filter(p -> !p.equals(root)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

}
