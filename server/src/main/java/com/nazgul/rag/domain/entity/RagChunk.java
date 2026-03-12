package com.nazgul.rag.domain.entity;

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
public class RagChunk {

    private UUID id;
    private UUID documentId;
    private UUID chapterId;
    private Integer chunkIndex;
    private String text;
    private Integer tokenCount;
    private Integer startOffset;
    private Integer endOffset;
    private LocalDateTime updatedAt;
}
