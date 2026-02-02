package com.nazgul.domain.hobby.controller;

import com.nazgul.domain.hobby.dto.HobbyDto;
import com.nazgul.domain.hobby.service.HobbyService;
import com.nazgul.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hobbies")
@RequiredArgsConstructor
public class HobbyController {

    private final HobbyService hobbyService;

    @GetMapping
    public ResponseEntity<List<HobbyDto.HobbyResponse>> getAllHobbies() {
        return ResponseEntity.ok(hobbyService.getAllHobbies());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<HobbyDto.HobbyResponse>> getHobbiesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(hobbyService.getHobbiesByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<HobbyDto.HobbyResponse>> searchHobbies(@RequestParam String keyword) {
        return ResponseEntity.ok(hobbyService.searchHobbies(keyword));
    }

    @PostMapping
    public ResponseEntity<HobbyDto.HobbyResponse> createHobby(@RequestBody HobbyDto.CreateRequest request) {
        return ResponseEntity.ok(hobbyService.createHobby(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<HobbyDto.UserHobbyResponse>> getMyHobbies(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(hobbyService.getUserHobbies(userDetails.getUserId()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HobbyDto.UserHobbyResponse>> getUserHobbies(@PathVariable Long userId) {
        return ResponseEntity.ok(hobbyService.getUserHobbies(userId));
    }

    @PostMapping("/my")
    public ResponseEntity<HobbyDto.UserHobbyResponse> addMyHobby(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody HobbyDto.AddUserHobbyRequest request) {
        return ResponseEntity.ok(hobbyService.addUserHobby(userDetails.getUserId(), request));
    }

    @DeleteMapping("/my/{hobbyId}")
    public ResponseEntity<Void> removeMyHobby(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long hobbyId) {
        hobbyService.removeUserHobby(userDetails.getUserId(), hobbyId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/my/{hobbyId}/skill")
    public ResponseEntity<HobbyDto.UserHobbyResponse> updateSkillLevel(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long hobbyId,
            @RequestParam Integer level) {
        return ResponseEntity.ok(hobbyService.updateSkillLevel(userDetails.getUserId(), hobbyId, level));
    }
}
