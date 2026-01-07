package com.boardhub.boardhub.global.config;

import com.boardhub.boardhub.global.jwt.JwtAuthenticationFilter;
import com.boardhub.boardhub.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // ✅ 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 안 씀 (토큰 방식)
                .authorizeHttpRequests(auth -> auth
                        // ✅ 중요: 회원가입, 로그인은 누구나 가능
                        .requestMatchers("/api/members/join", "/api/members/login", "/api/health").permitAll()
                        // ✅ 그 외 모든 요청(내 정보 조회 등)은 로그인해야 가능
                        .anyRequest().authenticated()
                )
                // ✅ 필터 추가: ID/PW 검사하기 전에 "토큰 검사"부터 해라
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}