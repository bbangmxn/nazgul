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
public class UserHobby {

    private Long id;
    private Long userId;
    private Long hobbyId;
    private Integer skillLevel;
    private LocalDateTime updatedAt;
}
