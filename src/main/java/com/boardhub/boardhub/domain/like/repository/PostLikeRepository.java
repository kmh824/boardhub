package com.boardhub.boardhub.domain.like.repository;

import com.boardhub.boardhub.domain.like.entity.PostLike;
import com.boardhub.boardhub.domain.like.entity.PostLikeId; // PostLikeId import 확인
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    // 특정 멤버가 특정 글을 추천했는지 확인
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
}