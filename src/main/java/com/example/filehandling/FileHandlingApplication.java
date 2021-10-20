package com.example.filehandling;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.filehandling.service.FileHandlingService;

@SpringBootApplication
public class FileHandlingApplication{

	@Resource
	FileHandlingService fileHandlingService;
	
	public static void main(String[] args) {
		SpringApplication.run(FileHandlingApplication.class, args);
	}

}
