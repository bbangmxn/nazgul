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
public class Follow {

    private Long id;
    private Long followerUserId;
    private Long followeeUserId;
    private LocalDateTime createdAt;
}
