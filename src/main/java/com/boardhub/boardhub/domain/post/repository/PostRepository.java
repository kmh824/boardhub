package com.boardhub.boardhub.domain.post.repository;

import com.boardhub.boardhub.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    // ✅ 게시판 코드(free, humor)로 글 목록 찾기 (최신순)
    List<Post> findByBoard_CodeOrderByIdDesc(String boardCode);

    // (기존 전체 조회용 - 필요 시 사용)
    List<Post> findAllByOrderByIdDesc();
}