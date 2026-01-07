package com.boardhub.boardhub.domain.member.service;

import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import com.boardhub.boardhub.global.jwt.JwtTokenProvider;
import com.boardhub.boardhub.web.dto.member.MemberInfoResDto;
import com.boardhub.boardhub.web.dto.member.MemberJoinReqDto;
import com.boardhub.boardhub.web.dto.member.MemberLoginReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(MemberJoinReqDto reqDto) {
        // 1. 이메일 중복 검사 (이미 가입된 이메일인지?)
        if (memberRepository.findByEmail(reqDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2. 저장 (DTO -> Entity 변환 후 Save)
        Member savedMember = memberRepository.save(reqDto.toEntity(passwordEncoder));

        return savedMember.getId();
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginReqDto reqDto) {
        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(reqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 검증 (DB 비번 vs 입력 비번)
        if (!passwordEncoder.matches(reqDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 생성 및 반환
        return jwtTokenProvider.createToken(member.getEmail(), member.getRoleKey());
    }

    // ✅ 내 정보 조회 로직
    @Transactional(readOnly = true)
    public MemberInfoResDto getMyInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new MemberInfoResDto(member);
    }
}