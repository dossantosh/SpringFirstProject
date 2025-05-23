package com.example.springfirstproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.springfirstproject.common.utils.EnvLoader;

@EnableTransactionManagement
@SpringBootApplication(
	exclude = {SecurityAutoConfiguration.class},
	scanBasePackages = {
    "com.example.springfirstproject.common",
	"com.example.springfirstproject.objects",
    "com.example.springfirstproject.permisos",
    "com.example.springfirstproject.user"
})
public class SpringFirstProject {

	public static void main(String[] args) {
		EnvLoader.loadEnv();
		SpringApplication.run(SpringFirstProject.class, args);
	}

}