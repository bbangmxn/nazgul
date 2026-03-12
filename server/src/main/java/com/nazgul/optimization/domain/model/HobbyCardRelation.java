package com.nazgul.optimization.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HobbyCardRelation {

    private UUID id;
    private UUID sourceCardId;
    private UUID targetCardId;
    private String relationType;
    private Double weight;
    private UUID evidenceChunkId;
    private LocalDateTime updatedAt;
}
