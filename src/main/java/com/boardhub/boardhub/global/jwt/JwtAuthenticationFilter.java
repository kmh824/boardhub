package com.boardhub.boardhub.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청에서 "accessToken" 쿠키 찾기
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        // 2. 토큰이 있고 유효하다면 -> 인증 정보(신분증) 만들어주기
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmail(token);

            // (간단하게 구현하기 위해 DB 조회 없이 토큰 정보로만 인증 객체 생성)
            // 실무에서는 여기서 DB를 조회해 상세 정보를 채우기도 합니다.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) // 일단 모든 사용자를 USER로 취급
            );

            // 3. 스프링 시큐리티에게 "이 사람 인증됨!" 하고 알려줌
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 4. 다음 단계로 통과
        filterChain.doFilter(request, response);
    }
}