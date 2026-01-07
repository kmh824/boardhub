package com.boardhub.boardhub.web.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginReqDto {
    private String email;
    private String password;
}