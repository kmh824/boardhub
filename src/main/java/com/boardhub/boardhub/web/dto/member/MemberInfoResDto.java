package com.boardhub.boardhub.web.dto.member;

import com.boardhub.boardhub.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberInfoResDto {
    private String email;
    private String nickname;
    private String username;

    public MemberInfoResDto(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
    }
}