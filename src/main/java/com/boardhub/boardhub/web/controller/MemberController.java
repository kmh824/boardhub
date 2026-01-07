package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.member.service.MemberService;
import com.boardhub.boardhub.web.dto.member.MemberJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boardhub.boardhub.web.dto.member.MemberLoginReqDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinReqDto reqDto) {
        memberService.join(reqDto);
        return ResponseEntity.ok("회원가입 성공! 🎉");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberLoginReqDto reqDto, HttpServletResponse response) {
        // 1. 서비스에서 로그인 시도 후 토큰 받아옴
        String token = memberService.login(reqDto);

        // 2. 토큰을 "쿠키"에 담기 (HttpOnly 설정으로 보안 강화)
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true); // 자바스크립트에서 접근 불가 (XSS 방지)
        cookie.setPath("/");      // 모든 경로에서 쿠키 사용
        cookie.setMaxAge(60 * 60); // 1시간 유지

        // 3. 응답에 쿠키 추가
        response.addCookie(cookie);

        return ResponseEntity.ok("로그인 성공! 🔑");
    }
}