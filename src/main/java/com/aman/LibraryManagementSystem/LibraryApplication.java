package com.aman.LibraryManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		System.out.println("Welcome to library management system");
		SpringApplication.run(LibraryApplication.class, args);
	}

}
