package com.boardhub.boardhub.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // HTML이 아니라 데이터(JSON/Text)를 반환하는 컨트롤러
public class HealthController {

    @GetMapping("/api/health")
    public String healthCheck() {
        return "백엔드와 통신 성공! 🚀";
    }
}