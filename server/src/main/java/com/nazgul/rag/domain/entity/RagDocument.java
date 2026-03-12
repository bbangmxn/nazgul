package com.nazgul.rag.domain.entity;

import com.nazgul.rag.domain.enums.RagDocumentSourceType;
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
public class RagDocument {

    private UUID id;
    private String sourceKey;
    private RagDocumentSourceType sourceType;
    private String title;
    private String contentHash;
    private LocalDateTime indexedAt;
    private LocalDateTime updatedAt;
}
