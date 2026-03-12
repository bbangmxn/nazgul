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
public class ContentChunk {

    private UUID id;
    private String sourceType;
    private Long sourceId;
    private Long hobbyId;
    private Integer chunkIndex;
    private String text;
    private Integer tokenCount;
    private Integer startOffset;
    private Integer endOffset;
    private String contentHash;
    private LocalDateTime updatedAt;
}
