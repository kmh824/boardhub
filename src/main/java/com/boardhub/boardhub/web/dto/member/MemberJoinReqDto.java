package com.boardhub.boardhub.web.dto.member;

import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class MemberJoinReqDto {
    private String email;
    private String password;
    private String username; // 실명
    private String nickname; // 별명

    // DTO -> Entity 변환 메서드 (비밀번호 암호화 포함)
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 🔒 암호화!
                .username(username)
                .nickname(nickname)
                .role(Role.USER) // 가입 시 기본 권한은 USER
                .build();
    }
}