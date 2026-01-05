package com.boardhub.boardhub.domain.board.entity;

import com.boardhub.boardhub.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // 게시판 URL에 쓰일 코드 (예: "free", "humor")

    @Column(nullable = false)
    private String name; // 사용자에게 보여질 이름 (예: "자유게시판")

    private String description; // 게시판 설명

    @Builder
    public Board(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
}