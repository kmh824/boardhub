package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.member.service.MemberService;
import com.boardhub.boardhub.web.dto.member.MemberJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boardhub.boardhub.web.dto.member.MemberLoginReqDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.boardhub.boardhub.web.dto.member.MemberInfoResDto;
import java.security.Principal;
import org.springframework.web.bind.annotation.PostMapping;

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

    // ✅ 내 정보 조회 API
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResDto> getMyInfo(Principal principal) {
        // principal.getName()에는 아까 필터에서 넣은 "email"이 들어있음
        String email = principal.getName();

        // 이메일로 DB 조회 (Service에 메서드 추가 필요 없이 Repository 바로 호출해도 됨, 간단하니까)
        // 하지만 정석대로 Service 거쳐서 가져오겠습니다.
        MemberInfoResDto memberInfo = memberService.getMyInfo(email);

        return ResponseEntity.ok(memberInfo);
    }

    // ✅ 로그아웃 API (쿠키 삭제)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 같은 이름("accessToken")의 쿠키를 만들고, 수명을 0으로 설정해서 덮어씌움
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 수명 0초 = 즉시 삭제

        response.addCookie(cookie);
        return ResponseEntity.ok("로그아웃 성공");
    }
}