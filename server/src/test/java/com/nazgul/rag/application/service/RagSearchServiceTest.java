package com.nazgul.rag.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.nazgul.rag.application.port.in.SearchRagQuery;
import com.nazgul.rag.application.port.in.SearchRagResult;
import com.nazgul.rag.application.port.out.LoadRagCardPort;
import com.nazgul.rag.application.port.out.LoadRagChunkPort;
import com.nazgul.rag.application.port.out.SaveRagQueryTracePort;
import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.entity.RagQueryTrace;
import com.nazgul.rag.domain.enums.RagCardType;
import com.nazgul.rag.domain.vo.RetrievalScope;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RagSearchServiceTest {

    @Test
    void searchUsesCardScopeWhenCardsExist() {
        UUID documentId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        FakeCardPort cardPort = new FakeCardPort(List.of(
                RagCard.builder()
                        .id(UUID.randomUUID())
                        .documentId(documentId)
                        .chapterId(chapterId)
                        .type(RagCardType.CONCEPT)
                        .title("Entity Card")
                        .summary("Card first retrieval")
                        .claims(List.of("Entity card narrows search scope"))
                        .tags(List.of("rag", "entity"))
                        .evidenceRefs(List.of())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ));
        FakeChunkPort chunkPort = new FakeChunkPort(List.of(
                RagChunk.builder()
                        .id(UUID.randomUUID())
                        .documentId(documentId)
                        .chapterId(chapterId)
                        .chunkIndex(1)
                        .text("Entity Card is the compressed retrieval unit.")
                        .updatedAt(LocalDateTime.now())
                        .build()
        ), true);
        FakeTracePort tracePort = new FakeTracePort();
        RagSearchService service = new RagSearchService(cardPort, chunkPort, tracePort);

        SearchRagResult result = service.search(new SearchRagQuery("entity", 5, 5));

        assertThat(result.cards()).hasSize(1);
        assertThat(result.scope().documentIds()).containsExactly(documentId);
        assertThat(result.scope().chapterIds()).containsExactly(chapterId);
        assertThat(chunkPort.lastScope.documentIds()).containsExactly(documentId);
        assertThat(chunkPort.lastScope.chapterIds()).containsExactly(chapterId);
        assertThat(tracePort.savedTraces).hasSize(1);
        assertThat(tracePort.savedTraces.get(0).getScopeDocumentIds()).containsExactly(documentId);
    }

    @Test
    void searchFallsBackToGlobalChunkSearchWhenScopedChunksAreMissing() {
        UUID documentId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        FakeCardPort cardPort = new FakeCardPort(List.of(
                RagCard.builder()
                        .id(UUID.randomUUID())
                        .documentId(documentId)
                        .chapterId(chapterId)
                        .type(RagCardType.CONCEPT)
                        .title("Scoped card")
                        .summary("Only scope")
                        .claims(List.of())
                        .tags(List.of())
                        .evidenceRefs(List.of())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ));
        FakeChunkPort chunkPort = new FakeChunkPort(List.of(), false);
        FakeTracePort tracePort = new FakeTracePort();
        RagSearchService service = new RagSearchService(cardPort, chunkPort, tracePort);

        SearchRagResult result = service.search(new SearchRagQuery("rag", 5, 5));

        assertThat(chunkPort.scopes).hasSize(2);
        assertThat(chunkPort.scopes.get(0).isGlobal()).isFalse();
        assertThat(chunkPort.scopes.get(1).isGlobal()).isTrue();
        assertThat(result.scope().isGlobal()).isTrue();
    }

    private static class FakeCardPort implements LoadRagCardPort {

        private final List<RagCard> cards;

        private FakeCardPort(List<RagCard> cards) {
            this.cards = cards;
        }

        @Override
        public List<RagCard> searchCards(String query, int limit) {
            return cards;
        }
    }

    private static class FakeChunkPort implements LoadRagChunkPort {

        private final List<RagChunk> chunks;
        private final boolean returnScopedChunks;
        private RetrievalScope lastScope = RetrievalScope.global();
        private final List<RetrievalScope> scopes = new ArrayList<>();

        private FakeChunkPort(List<RagChunk> chunks, boolean returnScopedChunks) {
            this.chunks = chunks;
            this.returnScopedChunks = returnScopedChunks;
        }

        @Override
        public List<RagChunk> searchChunks(String query, RetrievalScope scope, int limit) {
            this.lastScope = scope;
            this.scopes.add(scope);

            if (!scope.isGlobal() && !returnScopedChunks) {
                return List.of();
            }

            return chunks;
        }
    }

    private static class FakeTracePort implements SaveRagQueryTracePort {

        private final List<RagQueryTrace> savedTraces = new ArrayList<>();

        @Override
        public RagQueryTrace save(RagQueryTrace trace) {
            savedTraces.add(trace);
            return trace;
        }
    }
}
