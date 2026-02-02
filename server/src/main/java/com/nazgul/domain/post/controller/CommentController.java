package com.nazgul.domain.post.controller;

import com.nazgul.domain.post.dto.CommentDto;
import com.nazgul.domain.post.service.CommentService;
import com.nazgul.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto.CommentResponse> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentDto.CreateRequest request) {
        return ResponseEntity.ok(commentService.createComment(postId, userDetails.getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<Page<CommentDto.CommentResponse>> getComments(
            @PathVariable Long postId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(postId, pageable));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto.CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentDto.UpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, userDetails.getUserId(), request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}
