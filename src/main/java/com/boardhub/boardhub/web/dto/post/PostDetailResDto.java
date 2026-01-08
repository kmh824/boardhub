package com.boardhub.boardhub.web.dto.post;

import com.boardhub.boardhub.domain.post.entity.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostDetailResDto {
    private Long id;
    private String title;
    private String content; // 본문 내용 포함
    private String author;
    private String boardName;
    private LocalDateTime modifiedDate;
    private long viewCount; // 조회수
    private String authorEmail; // ✅ [추가] 작성자 이메일 (본인 확인용)

    public PostDetailResDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getMember().getNickname();
        this.boardName = entity.getBoard().getName();
        this.modifiedDate = entity.getUpdatedAt(); // BaseEntity 필드명 주의 (updatedAt)
        this.viewCount = entity.getViewCount();
        this.authorEmail = entity.getMember().getEmail(); // ✅ [추가] 이메일 담기
    }
}