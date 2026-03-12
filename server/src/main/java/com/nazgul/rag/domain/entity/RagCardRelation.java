package com.nazgul.rag.domain.entity;

import com.nazgul.rag.domain.enums.RagRelationType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RagCardRelation {

    private UUID id;
    private UUID sourceCardId;
    private UUID targetCardId;
    private RagRelationType relationType;
    private Double weight;
    private LocalDateTime updatedAt;
}
