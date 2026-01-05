package com.boardhub.boardhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardhubApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardhubApplication.class, args);
	}
}
