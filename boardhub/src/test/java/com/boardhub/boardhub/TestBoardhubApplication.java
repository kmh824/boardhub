package com.boardhub.boardhub;

import org.springframework.boot.SpringApplication;

public class TestBoardhubApplication {

	public static void main(String[] args) {
		SpringApplication.from(BoardhubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
