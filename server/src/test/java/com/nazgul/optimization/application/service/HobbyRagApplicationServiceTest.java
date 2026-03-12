package com.nazgul.optimization.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.nazgul.optimization.adapter.out.memory.InMemoryContentChunkStore;
import com.nazgul.optimization.adapter.out.memory.InMemoryHobbyCardRelationStore;
import com.nazgul.optimization.adapter.out.memory.InMemoryHobbyCardStore;
import com.nazgul.optimization.adapter.out.memory.InMemoryRecommendationTraceStore;
import com.nazgul.optimization.application.port.in.IngestHobbyContentCommand;
import com.nazgul.optimization.application.port.in.IngestHobbyContentResult;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationCommand;
import com.nazgul.optimization.application.port.in.RecommendPostsQuery;
import com.nazgul.optimization.application.port.in.SearchHobbyRagQuery;
import com.nazgul.optimization.application.port.in.SearchHobbyRagResult;
import com.nazgul.optimization.application.port.out.HobbyRagRerankPort;
import com.nazgul.optimization.application.port.out.HobbyRagSummaryPort;
import org.junit.jupiter.api.Test;

class HobbyRagApplicationServiceTest {

    @Test
    void ingestCreatesCardAndChunks() {
        HobbyRagApplicationService service = createService();

        IngestHobbyContentResult result = service.ingest(new IngestHobbyContentCommand(
                "POST",
                100L,
                10L,
                "TOPIC",
                "러닝 입문 팁",
                "러닝은 처음에는 짧게 시작하는 게 좋다. 주 3회로 적응하고 점진적으로 거리를 늘려야 한다.",
                java.util.List.of("running", "beginner"),
                "public"
        ));

        assertThat(result.card().getTitle()).isEqualTo("러닝 입문 팁");
        assertThat(result.card().getHobbyId()).isEqualTo(10L);
        assertThat(result.card().getEvidenceChunkIds()).isNotEmpty();
        assertThat(result.chunks()).isNotEmpty();
        assertThat(result.chunks().get(0).getSourceId()).isEqualTo(100L);
    }

    @Test
    void searchReturnsSummaryAndRecommendedPosts() {
        HobbyRagApplicationService service = createService();

        IngestHobbyContentResult running = service.ingest(new IngestHobbyContentCommand(
                "POST",
                101L,
                10L,
                "TOPIC",
                "러닝 루틴",
                "러닝은 주 3회 반복이 중요하다. 무리하지 말고 회복 시간을 둬야 한다.",
                java.util.List.of("running", "routine"),
                "public"
        ));
        IngestHobbyContentResult shoes = service.ingest(new IngestHobbyContentCommand(
                "POST",
                102L,
                10L,
                "TIP",
                "러닝화 선택",
                "발에 맞는 러닝화를 고르면 부상 위험을 줄일 수 있다.",
                java.util.List.of("running", "shoes"),
                "public"
        ));

        service.link(new LinkHobbyCardRelationCommand(
                running.card().getId(),
                shoes.card().getId(),
                "RELATED_TO",
                0.9d,
                shoes.chunks().get(0).getId()
        ));

        SearchHobbyRagResult result = service.search(new SearchHobbyRagQuery(
                1L,
                10L,
                101L,
                "러닝 초보 루틴",
                5
        ));

        assertThat(result.answer()).contains("요약");
        assertThat(result.cards()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.recommendedPostIds()).contains(101L);
        assertThat(result.traceId()).isNotNull();
    }

    @Test
    void recommendReturnsPostIds() {
        HobbyRagApplicationService service = createService();

        service.ingest(new IngestHobbyContentCommand(
                "POST",
                201L,
                20L,
                "TOPIC",
                "홈카페 기본",
                "홈카페는 원두와 추출 비율이 중요하다.",
                java.util.List.of("coffee"),
                "public"
        ));

        var result = service.recommend(new RecommendPostsQuery(
                2L,
                20L,
                "홈카페 원두",
                null,
                5
        ));

        assertThat(result.recommendedPostIds()).contains(201L);
        assertThat(result.recommendedCards()).isNotEmpty();
    }

    private HobbyRagApplicationService createService() {
        HobbyRagSummaryPort summaryPort = (query, hobbyId, cards, chunks) ->
                "요약: " + query + " / cards=" + cards.size() + " / chunks=" + chunks.size();
        HobbyRagRerankPort rerankPort = new HobbyRagRerankPort() {
            @Override
            public java.util.List<com.nazgul.optimization.domain.model.HobbyCard> rerankCards(
                    String query,
                    java.util.List<com.nazgul.optimization.domain.model.HobbyCard> candidates,
                    int limit
            ) {
                return candidates.stream().limit(limit).toList();
            }

            @Override
            public java.util.List<com.nazgul.optimization.domain.model.ContentChunk> rerankChunks(
                    String query,
                    java.util.List<com.nazgul.optimization.domain.model.ContentChunk> candidates,
                    int limit
            ) {
                return candidates.stream().limit(limit).toList();
            }
        };

        return new HobbyRagApplicationService(
                new InMemoryContentChunkStore(),
                new InMemoryHobbyCardStore(),
                new InMemoryHobbyCardRelationStore(),
                new InMemoryRecommendationTraceStore(),
                rerankPort,
                summaryPort
        );
    }
}
