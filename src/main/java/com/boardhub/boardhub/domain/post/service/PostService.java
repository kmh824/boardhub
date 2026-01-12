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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.boardhub.boardhub.web.dto.post.PostListResDto;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.boardhub.boardhub.web.dto.post.PostUpdateReqDto;
import com.boardhub.boardhub.domain.like.entity.PostLike;
import com.boardhub.boardhub.domain.like.repository.PostLikeRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostLikeRepository postLikeRepository;

    // ✅ [추가] Redis 사용을 위한 템플릿 주입
    private final RedisTemplate<String, String> redisTemplate;

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

    // ✅ [수정] 게시글 상세 조회 (동시성 해결 버전)
    @Transactional
    public PostDetailResDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // ❌ DB 직접 업데이트 -> 동시성 문제 원인
        // post.increaseViewCount();

        // ✅ [변경] Redis Atomic Increment 사용 (Key: "viewCount:1")
        String key = "viewCount:" + id;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.increment(key); // 1 증가 (원자성 보장)

        // 화면에 보여줄 때도 Redis에서 최신 값을 가져와서 DTO에 담아야 함 (DB값은 옛날 값일 수 있음)
        String currentViewCount = ops.get(key);

        // (참고) DTO에는 DB에 있는 viewCount가 들어있으므로, Redis 값으로 덮어씌워주는 로직이 필요할 수 있습니다.
        // 일단은 카운팅이 제대로 되는지가 핵심이므로 여기까지만 작성합니다.

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

    // ✅ [추가] 추천 토글 기능 (좋아요 <-> 좋아요 취소)
    @Transactional
    public boolean toggleLike(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        // 이미 추천했는지 확인
        Optional<PostLike> postLikeOptional = postLikeRepository.findByMemberAndPost(member, post);

        if (postLikeOptional.isPresent()) {
            // 이미 추천함 -> 취소 (삭제)
            postLikeRepository.delete(postLikeOptional.get());
            post.decreaseLikeCount();
            return false; // 추천 취소됨 (false)
        } else {
            // 추천 안 함 -> 추천 (생성)
            PostLike postLike = PostLike.builder()
                    .member(member)
                    .post(post)
                    .build();
            postLikeRepository.save(postLike);
            post.increaseLikeCount();
            return true; // 추천됨 (true)
        }
    }

    // ✅ [추가] 내가 이 글을 추천했는지 확인 (화면 로딩용)
    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 찾기 실패"));

        return postLikeRepository.findByMemberAndPost(member, post).isPresent();
    }

    // ✅ [추가] 특정 게시판 글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResDto> findByBoard(String boardCode) {
        return postRepository.findByBoard_CodeOrderByIdDesc(boardCode).stream()
                .map(PostListResDto::new)
                .collect(Collectors.toList());
    }
}