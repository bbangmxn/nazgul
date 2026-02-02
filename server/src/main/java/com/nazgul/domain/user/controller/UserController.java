package com.nazgul.domain.user.controller;

import com.nazgul.domain.user.dto.UserDto;
import com.nazgul.domain.user.service.UserService;
import com.nazgul.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto.UserResponse> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getUser(userDetails.getUserId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponse> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto.UserResponse> updateMe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserDto.UpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userDetails.getUserId(), request));
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> follow(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userId) {
        userService.follow(userDetails.getUserId(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollow(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long userId) {
        userService.unfollow(userDetails.getUserId(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserDto.UserSummary>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserDto.UserSummary>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowing(userId));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<UserDto.UserSummary>> getRecommendedUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getRecommendedUsers(userDetails.getUserId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto.UserSummary>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }
}
