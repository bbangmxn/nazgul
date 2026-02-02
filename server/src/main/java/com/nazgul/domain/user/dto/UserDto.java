package com.nazgul.domain.user.dto;

import com.nazgul.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpRequest {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String accessToken;
        private String tokenType;
        private UserResponse user;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String nickname;
        private String bio;
        private String profileImage;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserResponse {
        private Long id;
        private String email;
        private String nickname;
        private String bio;
        private String profileImage;
        private List<HobbyInfo> hobbies;
        private long followingCount;
        private long followersCount;
        private LocalDateTime createdAt;

        public static UserResponse from(User user, long followingCount, long followersCount) {
            return UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .bio(user.getBio())
                    .profileImage(user.getProfileImage())
                    .followingCount(followingCount)
                    .followersCount(followersCount)
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummary {
        private Long id;
        private String nickname;
        private String profileImage;

        public static UserSummary from(User user) {
            return UserSummary.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HobbyInfo {
        private Long id;
        private String name;
        private String icon;
        private Integer skillLevel;
    }
}
