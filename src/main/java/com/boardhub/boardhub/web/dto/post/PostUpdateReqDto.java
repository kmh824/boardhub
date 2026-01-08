package com.boardhub.boardhub.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateReqDto {
    private String title;
    private String content;
}