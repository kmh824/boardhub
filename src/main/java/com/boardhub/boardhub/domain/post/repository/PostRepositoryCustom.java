package com.boardhub.boardhub.domain.post.repository;

import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.web.dto.post.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllWithMemberAndBoard(); // 기존 코드

    // ✅ [추가] 검색 + 페이징 메서드
    Page<Post> search(PostSearchCondition condition, Pageable pageable);
}