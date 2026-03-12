package com.nazgul.optimization.application.port.in;

public record CreateContentChunkCommand(
        String sourceType,
        Long sourceId,
        Long hobbyId,
        Integer chunkIndex,
        String text,
        Integer tokenCount,
        Integer startOffset,
        Integer endOffset,
        String contentHash
) {
}
