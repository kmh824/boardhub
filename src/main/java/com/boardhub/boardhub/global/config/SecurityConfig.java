package com.boardhub.boardhub.global.config;

import com.boardhub.boardhub.global.jwt.JwtAuthenticationFilter;
import com.boardhub.boardhub.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // ✅ import 추가
import org.springframework.web.cors.CorsConfigurationSource; // ✅ import 추가
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // ✅ import 추가

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ [추가 1] 스프링 시큐리티가 CORS 설정을 사용하도록 지정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ [추가] 게시글 목록 조회(GET)는 누구나 허용
                        // (주의: POST는 글쓰기니까 로그인해야 함! 그래서 HttpMethod.GET만 콕 집어서 허용)
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()

                        .requestMatchers("/api/members/join", "/api/members/login", "/api/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ [추가 2] CORS 설정 구체화 (WebConfig 설정을 여기로 가져옴)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. 프론트엔드 주소 허용
        configuration.addAllowedOrigin("http://localhost:5173");

        // 2. 모든 HTTP 메서드 허용 (GET, POST, OPTIONS 등)
        configuration.addAllowedMethod("*");

        // 3. 모든 헤더 허용
        configuration.addAllowedHeader("*");

        // 4. 쿠키/인증 정보 포함 허용
        configuration.setAllowCredentials(true);

        // 5. 모든 경로에 대해 위 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}