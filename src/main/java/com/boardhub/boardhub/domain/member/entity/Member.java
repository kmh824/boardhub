package com.boardhub.boardhub.domain.member.entity;

import com.boardhub.boardhub.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ [추가됨] DTO에서 보내는 'username(실명)'을 받을 곳
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder // ✅ 생성자에도 username을 꼭 추가해야 DTO의 .builder().username(...)이 작동합니다.
    public Member(String email, String password, String username, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.username = username; // 여기 추가!
        this.nickname = nickname;
        this.role = role;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}