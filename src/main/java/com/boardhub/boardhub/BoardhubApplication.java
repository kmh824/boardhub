package com.boardhub.boardhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // ✅ 추가

@EnableScheduling // ✅ 스케줄러 기능 켜기
@SpringBootApplication
public class BoardhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardhubApplication.class, args);
	}

}