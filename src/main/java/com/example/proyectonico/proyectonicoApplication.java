package com.example.proyectonico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class proyectonicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(proyectonicoApplication.class, args);
	}

}
