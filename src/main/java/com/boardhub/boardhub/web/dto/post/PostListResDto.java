package com.boardhub.boardhub.web.dto.post;

import com.boardhub.boardhub.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResDto {
    private Long id;
    private String title;
    private String author;
    private String boardName; // "자유게시판" 같은 이름
    private LocalDateTime modifiedDate;

    public PostListResDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getMember().getNickname(); // 작성자 닉네임
        this.boardName = entity.getBoard().getName();   // 게시판 이름
        this.modifiedDate = entity.getUpdatedAt();
    }
}