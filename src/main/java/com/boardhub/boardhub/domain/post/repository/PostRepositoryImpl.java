package com.boardhub.boardhub.domain.post.repository;

import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.web.dto.post.PostSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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
                .join(post.member, member).fetchJoin()
                .join(post.board, board).fetchJoin()
                .orderBy(post.id.desc())
                .fetch();
    }

    // ✅ [추가] 동적 검색 구현
    @Override
    public Page<Post> search(PostSearchCondition condition, Pageable pageable) {

        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(post.board, board).fetchJoin()
                .where(
                        // ✅ 게시판 코드 조건 추가 (콤마로 구분하면 AND 조건이 됨)
                        boardCodeEq(condition.getBoardCode()),
                        searchCondition(condition)
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        // ✅ 카운트 쿼리에도 똑같이 추가
                        boardCodeEq(condition.getBoardCode()),
                        searchCondition(condition)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 🔍 검색 조건을 조립하는 메서드 (BooleanExpression 활용)
    private BooleanExpression searchCondition(PostSearchCondition condition) {
        if (condition == null || !StringUtils.hasText(condition.getKeyword())) {
            return null; // 검색어 없으면 전체 조회
        }

        String keyword = condition.getKeyword();
        String type = condition.getSearchType();

        if ("title".equals(type)) {
            return post.title.contains(keyword); // 제목 검색 (like '%keyword%')
        } else if ("content".equals(type)) {
            return post.content.contains(keyword); // 내용 검색
        } else if ("writer".equals(type)) {
            return member.nickname.contains(keyword); // 작성자 검색
        }

        // 기본: 제목 + 내용 검색
        return post.title.contains(keyword).or(post.content.contains(keyword));
    }

    // ✅ [추가] 게시판 코드 조건 판별 메서드
    private BooleanExpression boardCodeEq(String boardCode) {
        return StringUtils.hasText(boardCode) ? post.board.code.eq(boardCode) : null;
    }
}