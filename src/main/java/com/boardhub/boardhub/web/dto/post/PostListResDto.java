package com.boardhub.boardhub.web.dto.post;

import com.boardhub.boardhub.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResDto {
    private Long id;
    private String title;
    private String author;
    private String boardName;
    private LocalDateTime modifiedDate;

    // ✅ [추가] 조회수와 추천수 필드
    private long viewCount;
    private long likeCount;

    public PostListResDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getMember().getNickname();
        this.boardName = entity.getBoard().getName();
        this.modifiedDate = entity.getUpdatedAt();

        // ✅ [추가] 엔티티에서 값 꺼내서 담기
        this.viewCount = entity.getViewCount();
        this.likeCount = entity.getLikeCount();
    }
}