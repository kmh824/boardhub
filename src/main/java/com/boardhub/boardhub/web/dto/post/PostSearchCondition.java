package com.boardhub.boardhub.web.dto.post;

import lombok.Data;

@Data
public class PostSearchCondition {
    private String keyword;    // 검색어
    private String searchType; // 검색 타입 (제목, 내용 등)

    // ✅ [추가] 게시판 필터링을 위한 필드
    private String boardCode;
}