package com.boardhub.boardhub.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API 서버는 CSRF 보안 기능 불필요 (비활성화)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // "/api/"로 시작하는 주소는 로그인 없이 허용!
                        .anyRequest().authenticated() // 그 외 나머지 주소는 모두 로그인 필요
                );

        return http.build();
    }

    // ✅ [추가] 비밀번호 암호화 도구 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}