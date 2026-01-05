package com.boardhub.boardhub.domain.comment.entity;

import com.boardhub.boardhub.domain.common.BaseEntity;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 댓글이 달린 게시글 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 댓글 작성자 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // [대댓글 구조 핵심] 부모 댓글 (내 부모가 누구냐?)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 자식 댓글들 (내가 가진 대댓글들은 누구냐?) - 부모가 삭제되면 자식도 함께 삭제(cascade)
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, Post post, Member member, Comment parent) {
        this.content = content;
        this.post = post;
        this.member = member;
        this.parent = parent;
    }

    // 댓글 수정
    public void update(String content) {
        this.content = content;
    }
}