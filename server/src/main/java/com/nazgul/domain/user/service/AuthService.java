package com.nazgul.domain.user.service;

import com.nazgul.domain.user.dto.UserDto;
import com.nazgul.domain.user.entity.User;
import com.nazgul.domain.user.repository.FollowRepository;
import com.nazgul.domain.user.repository.UserRepository;
import com.nazgul.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserDto.UserResponse signUp(UserDto.SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.UserResponse.from(savedUser, 0, 0);
    }

    public UserDto.LoginResponse login(UserDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail());
        
        long followingCount = followRepository.countByFollower(user);
        long followersCount = followRepository.countByFollowing(user);

        return UserDto.LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(UserDto.UserResponse.from(user, followingCount, followersCount))
                .build();
    }
}
