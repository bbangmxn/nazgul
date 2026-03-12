package com.nazgul.rag.application.service;

import com.nazgul.rag.application.port.in.SearchRagQuery;
import com.nazgul.rag.application.port.in.SearchRagResult;
import com.nazgul.rag.application.port.in.SearchRagUseCase;
import com.nazgul.rag.application.port.out.LoadRagCardPort;
import com.nazgul.rag.application.port.out.LoadRagChunkPort;
import com.nazgul.rag.application.port.out.SaveRagQueryTracePort;
import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.entity.RagQueryTrace;
import com.nazgul.rag.domain.vo.RetrievalScope;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RagSearchService implements SearchRagUseCase {

    private static final int MAX_LIMIT = 20;

    private final LoadRagCardPort loadRagCardPort;
    private final LoadRagChunkPort loadRagChunkPort;
    private final SaveRagQueryTracePort saveRagQueryTracePort;

    @Override
    @Transactional
    public SearchRagResult search(SearchRagQuery query) {
        String normalizedQuery = normalizeQuery(query.query());
        int cardLimit = normalizeLimit(query.cardLimit(), 5);
        int evidenceLimit = normalizeLimit(query.evidenceLimit(), 6);

        long startedAt = System.currentTimeMillis();

        List<RagCard> cards = loadRagCardPort.searchCards(normalizedQuery, cardLimit);
        RetrievalScope scope = createScope(cards);
        List<RagChunk> chunks = loadRagChunkPort.searchChunks(normalizedQuery, scope, evidenceLimit);

        if (!scope.isGlobal() && chunks.isEmpty()) {
            chunks = loadRagChunkPort.searchChunks(normalizedQuery, RetrievalScope.global(), evidenceLimit);
            scope = RetrievalScope.global();
        }

        long latencyMs = System.currentTimeMillis() - startedAt;
        saveRagQueryTracePort.save(RagQueryTrace.of(
                normalizedQuery,
                scope,
                cards.stream().map(RagCard::getId).toList(),
                chunks.stream().map(RagChunk::getId).toList(),
                cards.size() + chunks.size(),
                latencyMs
        ));

        return new SearchRagResult(normalizedQuery, scope, cards, chunks);
    }

    private RetrievalScope createScope(List<RagCard> cards) {
        Set<UUID> documentIds = new LinkedHashSet<>();
        Set<UUID> chapterIds = new LinkedHashSet<>();

        for (RagCard card : cards) {
            if (card.getDocumentId() != null) {
                documentIds.add(card.getDocumentId());
            }
            if (card.getChapterId() != null) {
                chapterIds.add(card.getChapterId());
            }
        }

        return new RetrievalScope(documentIds, chapterIds);
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("검색어는 비어 있을 수 없습니다.");
        }

        return query.trim();
    }

    private int normalizeLimit(int requestedLimit, int defaultLimit) {
        if (requestedLimit <= 0) {
            return defaultLimit;
        }

        return Math.min(requestedLimit, MAX_LIMIT);
    }
}
