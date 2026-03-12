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
public class User {

    private Long id;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
}
