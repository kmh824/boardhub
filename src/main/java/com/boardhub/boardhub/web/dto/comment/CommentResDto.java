package com.boardhub.boardhub.web.dto.comment;

import com.boardhub.boardhub.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResDto {
    private Long id;
    private String content;
    private String author;
    private String authorEmail; // 본인 삭제 버튼용
    private String createdDate;
    private List<CommentResDto> children; // ✅ 대댓글 리스트 (재귀 구조)

    public CommentResDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.authorEmail = comment.getMember().getEmail();
        this.createdDate = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        // 자식 댓글들도 DTO로 변환해서 담기
        this.children = comment.getChildren().stream()
                .map(CommentResDto::new)
                .collect(Collectors.toList());
    }
}