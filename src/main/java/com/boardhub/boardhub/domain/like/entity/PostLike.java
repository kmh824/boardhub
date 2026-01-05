package com.boardhub.boardhub.domain.like.entity;

import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(PostLikeId.class) // "이 엔티티는 복합키(PostLikeId)를 씁니다" 선언
@EntityListeners(AuditingEntityListener.class) // 생성 시간 자동 기록
public class PostLike {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @CreatedDate // 좋아요 누른 시간
    private LocalDateTime createdAt;

    @Builder
    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}