package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.post.service.PostService;
import com.boardhub.boardhub.web.dto.post.PostDetailResDto;
import com.boardhub.boardhub.web.dto.post.PostUpdateReqDto;
import com.boardhub.boardhub.web.dto.post.PostWriteReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boardhub.boardhub.web.dto.post.PostListResDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import com.boardhub.boardhub.web.dto.post.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> write(@RequestBody PostWriteReqDto reqDto, Principal principal) {
        postService.write(principal.getName(), reqDto);
        return ResponseEntity.ok("게시글 작성 성공! 📝");
    }

    // ✅ [추가] 게시글 목록 조회 API
    @GetMapping
    public ResponseEntity<List<PostListResDto>> findAll() {
        return ResponseEntity.ok(postService.findAllDesc());
    }

    // ✅ [추가] 게시글 상세 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    // ✅ [추가] 게시글 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody PostUpdateReqDto reqDto,
                                         Principal principal) {
        postService.update(id, principal.getName(), reqDto);
        return ResponseEntity.ok("게시글이 수정되었습니다. ✏️");
    }

    // ✅ [추가] 게시글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, Principal principal) {
        postService.delete(id, principal.getName());
        return ResponseEntity.ok("게시글이 삭제되었습니다. 🗑️");
    }

    // ✅ [추가] 추천 토글 API
    @PostMapping("/{id}/like")
    public ResponseEntity<String> like(@PathVariable Long id, Principal principal) {
        boolean liked = postService.toggleLike(id, principal.getName());
        return ResponseEntity.ok(liked ? "추천했습니다! 👍" : "추천을 취소했습니다.");
    }

    // ✅ [추가] 내 추천 여부 확인 API
    @GetMapping("/{id}/like")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(postService.isLiked(id, principal.getName()));
    }

    // ✅ [추가] 특정 게시판 글 목록 조회 API
    // 요청 예시: GET /api/posts?boardCode=free
    @GetMapping(params = "boardCode")
    public ResponseEntity<List<PostListResDto>> findByBoard(@RequestParam String boardCode) {
        return ResponseEntity.ok(postService.findByBoard(boardCode));
    }

    @GetMapping("/api/posts/search")
    public Page<PostListResDto> searchPosts(
            PostSearchCondition condition,
            @PageableDefault(size = 10) Pageable pageable) {
        return postService.search(condition, pageable);
    }
}