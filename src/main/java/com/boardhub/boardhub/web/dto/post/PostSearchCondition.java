package com.boardhub.boardhub.web.dto.post;

import lombok.Data;

@Data
public class PostSearchCondition {
    private String keyword;   // 검색어 (예: "스프링")
    private String searchType; // 검색 타입 (title, content, writer)
}