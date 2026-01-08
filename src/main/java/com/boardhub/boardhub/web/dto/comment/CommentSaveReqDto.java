package com.boardhub.boardhub.web.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveReqDto {
    private String content;
    private Long postId;
    private Long parentId; // 대댓글일 경우 부모 댓글 ID (없으면 null)
}