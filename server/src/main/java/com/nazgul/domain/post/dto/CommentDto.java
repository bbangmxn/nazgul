package com.nazgul.domain.post.dto;

import com.nazgul.domain.post.entity.Comment;
import com.nazgul.domain.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String content;
        private Long parentId;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentResponse {
        private Long id;
        private UserDto.UserSummary author;
        private String content;
        private Long parentId;
        private List<CommentResponse> replies;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CommentResponse from(Comment comment, List<CommentResponse> replies) {
            return CommentResponse.builder()
                    .id(comment.getId())
                    .author(UserDto.UserSummary.from(comment.getAuthor()))
                    .content(comment.getContent())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .replies(replies)
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
}
