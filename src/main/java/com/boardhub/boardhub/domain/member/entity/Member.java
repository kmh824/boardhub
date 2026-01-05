package com.boardhub.boardhub.domain.member.entity;

import com.boardhub.boardhub.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 접근 제한 (안전성)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 로그인 ID로 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING) // DB에 저장될 때 "USER" 문자열로 저장됨
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    // 회원 정보 수정 (비즈니스 로직)
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}