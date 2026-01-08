package com.boardhub.boardhub.domain.comment.repository;

import com.boardhub.boardhub.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글을 모두 가져오되, "부모 댓글이 없는(최상위)" 댓글들만 먼저 가져옴
    // (자식 댓글은 부모를 통해 줄줄이 소세지처럼 딸려오게 할 예정)
    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.id ASC")
    List<Comment> findByPostId(@Param("postId") Long postId);
}