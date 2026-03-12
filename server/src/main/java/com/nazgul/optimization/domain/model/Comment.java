package com.nazgul.optimization.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
