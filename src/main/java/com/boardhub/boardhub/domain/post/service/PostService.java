package com.boardhub.boardhub.domain.post.service;

import com.boardhub.boardhub.domain.board.entity.Board;
import com.boardhub.boardhub.domain.board.repository.BoardRepository;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.domain.post.repository.PostRepository;
import com.boardhub.boardhub.web.dto.post.PostDetailResDto;
import com.boardhub.boardhub.web.dto.post.PostWriteReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.boardhub.boardhub.web.dto.post.PostListResDto;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;
import com.boardhub.boardhub.web.dto.post.PostUpdateReqDto;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long write(String email, PostWriteReqDto reqDto) {
        // 1. 작성자 찾기 (로그인한 사람)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // 2. 게시판 찾기 ("free" -> 자유게시판 객체)
        Board board = boardRepository.findByCode(reqDto.getBoardCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다."));

        // 3. 게시글 생성 및 저장
        Post post = reqDto.toEntity(member, board);
        postRepository.save(post);

        return post.getId();
    }

    // ✅ [추가] 게시글 전체 조회 (최신순)
    @Transactional(readOnly = true)
    public List<PostListResDto> findAllDesc() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(PostListResDto::new) // 하나씩 DTO로 변환
                .collect(Collectors.toList());
    }

    // ✅ [추가] 게시글 상세 조회
    @Transactional
    public PostDetailResDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 조회수 1 증가 (변경 감지로 자동 저장됨)
        post.increaseViewCount();

        return new PostDetailResDto(post);
    }

    // ✅ [추가] 게시글 수정
    @Transactional
    public Long update(Long id, String email, PostUpdateReqDto reqDto) {
        // 1. 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 2. 작성자 검증 (로그인한 이메일 vs 글쓴이 이메일)
        if (!post.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 3. 수정 (더티 체킹: save 안 해도 트랜잭션 끝나면 자동 업데이트)
        post.update(reqDto.getTitle(), reqDto.getContent());

        return id;
    }

    // ✅ [추가] 게시글 삭제
    @Transactional
    public void delete(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 작성자 검증
        if (!post.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}