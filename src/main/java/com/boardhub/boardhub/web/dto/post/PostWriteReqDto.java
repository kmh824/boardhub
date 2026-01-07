package com.boardhub.boardhub.web.dto.post;

import com.boardhub.boardhub.domain.board.entity.Board;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWriteReqDto {
    private String title;
    private String content;
    private String boardCode; // "free" 또는 "humor" 같은 게시판 코드

    // DTO -> Entity 변환 메서드
    public Post toEntity(Member member, Board board) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member) // 글쓴이
                .board(board)   // 게시판 종류
                .build();
    }
}