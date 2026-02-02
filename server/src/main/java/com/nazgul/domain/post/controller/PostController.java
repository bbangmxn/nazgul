package com.nazgul.domain.post.controller;

import com.nazgul.domain.post.dto.PostDto;
import com.nazgul.domain.post.service.PostService;
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
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto.PostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PostDto.CreateRequest request) {
        return ResponseEntity.ok(postService.createPost(userDetails.getUserId(), request));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.PostResponse> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        return ResponseEntity.ok(postService.getPost(postId, userId));
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostDto.PostResponse>> getFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getFeed(userDetails.getUserId(), pageable));
    }

    @GetMapping("/hobby/{hobbyId}")
    public ResponseEntity<Page<PostDto.PostResponse>> getPostsByHobby(
            @PathVariable Long hobbyId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        return ResponseEntity.ok(postService.getPostsByHobby(hobbyId, userId, pageable));
    }

    @GetMapping("/recommended")
    public ResponseEntity<Page<PostDto.PostResponse>> getRecommendedPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getRecommendedPosts(userDetails.getUserId(), pageable));
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<PostDto.PostResponse>> getPopularPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        return ResponseEntity.ok(postService.getPopularPosts(userId, pageable));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto.PostResponse> updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PostDto.UpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, userDetails.getUserId(), request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(postId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.likePost(postId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.unlikePost(postId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}
