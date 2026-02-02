package com.nazgul.domain.post.dto;

import com.nazgul.domain.post.entity.Post;
import com.nazgul.domain.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

public class PostDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String content;
        private Long hobbyId;
        private String imageUrl;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String content;
        private String imageUrl;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostResponse {
        private Long id;
        private UserDto.UserSummary author;
        private String content;
        private String imageUrl;
        private HobbyInfo hobby;
        private long likeCount;
        private long commentCount;
        private boolean liked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PostResponse from(Post post, long likeCount, long commentCount, boolean liked) {
            HobbyInfo hobbyInfo = null;
            if (post.getHobby() != null) {
                hobbyInfo = HobbyInfo.builder()
                        .id(post.getHobby().getId())
                        .name(post.getHobby().getName())
                        .icon(post.getHobby().getIcon())
                        .build();
            }
            
            return PostResponse.builder()
                    .id(post.getId())
                    .author(UserDto.UserSummary.from(post.getAuthor()))
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .hobby(hobbyInfo)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .liked(liked)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
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
    }
}
