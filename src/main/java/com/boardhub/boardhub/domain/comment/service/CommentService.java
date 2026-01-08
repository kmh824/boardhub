package com.boardhub.boardhub.domain.comment.service;

import com.boardhub.boardhub.domain.comment.entity.Comment;
import com.boardhub.boardhub.domain.comment.repository.CommentRepository;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.domain.post.repository.PostRepository;
import com.boardhub.boardhub.web.dto.comment.CommentResDto;
import com.boardhub.boardhub.web.dto.comment.CommentSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 1. 댓글 쓰기
    @Transactional
    public Long save(String email, CommentSaveReqDto reqDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(reqDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment parent = null;
        // 대댓글인 경우 부모 댓글 찾기
        if (reqDto.getParentId() != null) {
            parent = commentRepository.findById(reqDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = Comment.builder()
                .content(reqDto.getContent())
                .member(member)
                .post(post)
                .parent(parent) // 부모 연결 (없으면 null)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 2. 댓글 목록 조회 (대댓글 포함)
    @Transactional(readOnly = true)
    public List<CommentResDto> findAll(Long postId) {
        // 부모가 없는 최상위 댓글만 가져오면, 자식들은 DTO 생성자에서 알아서 딸려옴
        return commentRepository.findByPostId(postId).stream()
                .map(CommentResDto::new)
                .collect(Collectors.toList());
    }

    // 3. 댓글 삭제
    @Transactional
    public void delete(Long id, String email) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!comment.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}