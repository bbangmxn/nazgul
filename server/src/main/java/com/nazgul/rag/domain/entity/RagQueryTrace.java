package com.nazgul.rag.domain.entity;

import com.nazgul.rag.domain.vo.RetrievalScope;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RagQueryTrace {

    private UUID id;
    private String query;
    private List<UUID> scopeDocumentIds;
    private List<UUID> scopeChapterIds;
    private List<UUID> selectedCardIds;
    private List<UUID> selectedChunkIds;
    private Integer totalHits;
    private Long latencyMs;
    private LocalDateTime createdAt;

    public static RagQueryTrace of(
            String query,
            RetrievalScope scope,
            List<UUID> selectedCardIds,
            List<UUID> selectedChunkIds,
            Integer totalHits,
            Long latencyMs
    ) {
        return RagQueryTrace.builder()
                .id(UUID.randomUUID())
                .query(query)
                .scopeDocumentIds(List.copyOf(scope.documentIds()))
                .scopeChapterIds(List.copyOf(scope.chapterIds()))
                .selectedCardIds(List.copyOf(selectedCardIds))
                .selectedChunkIds(List.copyOf(selectedChunkIds))
                .totalHits(totalHits)
                .latencyMs(latencyMs)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
