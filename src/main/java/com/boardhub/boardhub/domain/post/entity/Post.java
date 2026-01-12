package com.boardhub.boardhub.domain.post.entity;
import com.boardhub.boardhub.domain.board.entity.Board;
import com.boardhub.boardhub.domain.common.BaseEntity;
import com.boardhub.boardhub.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) // 본문은 길 수 있으므로 TEXT 타입
    private String content;

    private long viewCount; // 조회수

    @Column(columnDefinition = "integer default 0", nullable = false)
    private long likeCount; // 좋아요 수

    // 작성자 (Member 엔티티와 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 소속 게시판 (Board 엔티티와 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Post(String title, String content, Member member, Board board) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.board = board;
        this.viewCount = 0;
        this.likeCount = 0;
    }

    // 게시글 수정 편의 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // ✅ [추가] 조회수 증가 메서드
    public void increaseViewCount() {
        this.viewCount++;
    }

    // ✅ 추천수 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // ✅ 추천수 감소
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // ✅ [추가] Redis 값으로 조회수 동기화
    public void syncViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}