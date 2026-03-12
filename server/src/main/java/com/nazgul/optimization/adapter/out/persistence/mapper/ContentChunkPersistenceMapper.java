package com.nazgul.optimization.adapter.out.persistence.mapper;

import com.nazgul.optimization.adapter.out.persistence.entity.ContentChunkEntity;
import com.nazgul.optimization.domain.model.ContentChunk;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ContentChunkPersistenceMapper {

    public ContentChunk toDomain(ContentChunkEntity entity) {
        return ContentChunk.builder()
                .id(UUID.fromString(entity.getId()))
                .sourceType(entity.getSourceType())
                .sourceId(entity.getSourceId())
                .hobbyId(entity.getHobbyId())
                .chunkIndex(entity.getChunkIndex())
                .text(entity.getText())
                .tokenCount(entity.getTokenCount())
                .startOffset(entity.getStartOffset())
                .endOffset(entity.getEndOffset())
                .contentHash(entity.getContentHash())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ContentChunkEntity toEntity(ContentChunk chunk) {
        return ContentChunkEntity.builder()
                .id(chunk.getId().toString())
                .sourceType(chunk.getSourceType())
                .sourceId(chunk.getSourceId())
                .hobbyId(chunk.getHobbyId())
                .chunkIndex(chunk.getChunkIndex())
                .text(chunk.getText())
                .tokenCount(chunk.getTokenCount())
                .startOffset(chunk.getStartOffset())
                .endOffset(chunk.getEndOffset())
                .contentHash(chunk.getContentHash())
                .updatedAt(chunk.getUpdatedAt())
                .build();
    }
}
