package com.boardhub.boardhub.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 API 요청에 대해
                .allowedOrigins("http://localhost:5173") // 2. 프론트엔드 주소만 허용 (보안)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 3. 모든 HTTP 메서드 허용
                .allowCredentials(true); // 4. 쿠키/인증 정보 포함 허용 (로그인 필수 설정)
    }
}