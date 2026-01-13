package com.boardhub.boardhub.domain.post.repository;

import com.boardhub.boardhub.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

// QClass 임포트 (아까 생성된 파일)
import static com.boardhub.boardhub.domain.post.entity.QPost.post;
import static com.boardhub.boardhub.domain.member.entity.QMember.member;
import static com.boardhub.boardhub.domain.board.entity.QBoard.board;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllWithMemberAndBoard() {
        return queryFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin() // ✨ 여기가 핵심! (작성자 같이 가져와)
                .join(post.board, board).fetchJoin()   // ✨ 여기도 핵심! (게시판 정보 같이 가져와)
                .orderBy(post.id.desc())
                .fetch();
    }
}