package com.boardhub.boardhub.domain.post.repository;

import com.boardhub.boardhub.domain.post.entity.Post;
import java.util.List;

public interface PostRepositoryCustom {
    // N+1 문제를 해결할 메서드 선언
    List<Post> findAllWithMemberAndBoard();
}