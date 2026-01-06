package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.member.service.MemberService;
import com.boardhub.boardhub.web.dto.member.MemberJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}