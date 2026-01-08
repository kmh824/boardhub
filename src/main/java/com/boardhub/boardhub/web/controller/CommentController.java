package com.boardhub.boardhub.web.controller;

import com.boardhub.boardhub.domain.comment.service.CommentService;
import com.boardhub.boardhub.web.dto.comment.CommentResDto;
import com.boardhub.boardhub.web.dto.comment.CommentSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성
    @PostMapping
    public ResponseEntity<String> save(@RequestBody CommentSaveReqDto reqDto, Principal principal) {
        commentService.save(principal.getName(), reqDto);
        return ResponseEntity.ok("댓글이 등록되었습니다. 💬");
    }

    // 2. 댓글 목록 조회 (게시글 ID로 조회)
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResDto>> findAll(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findAll(postId));
    }

    // 3. 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, Principal principal) {
        commentService.delete(id, principal.getName());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}