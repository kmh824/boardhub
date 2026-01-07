package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.post.service.PostService;
import com.boardhub.boardhub.web.dto.post.PostDetailResDto;
import com.boardhub.boardhub.web.dto.post.PostWriteReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boardhub.boardhub.web.dto.post.PostListResDto;

import java.util.List;

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
}