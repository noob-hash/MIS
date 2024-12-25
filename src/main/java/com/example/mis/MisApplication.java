package com.example.mis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.mis.repo")
@EntityScan(basePackages = "com.example.mis.entity")
public class MisApplication {

	public static void main(String[] args) {
		SpringApplication.run(MisApplication.class, args);
	}

}
