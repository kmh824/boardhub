package com.boardhub.boardhub.domain.member.service;

import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import com.boardhub.boardhub.web.dto.member.MemberJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
}