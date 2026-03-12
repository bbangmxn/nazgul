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
public class Reaction {

    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;
    private String type;
    private LocalDateTime createdAt;
}
