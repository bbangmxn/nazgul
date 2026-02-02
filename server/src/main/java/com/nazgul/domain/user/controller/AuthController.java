package com.nazgul.domain.user.controller;

import com.nazgul.domain.user.dto.UserDto;
import com.nazgul.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto.UserResponse> signUp(@RequestBody UserDto.SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@RequestBody UserDto.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
