package com.nazgul.optimization.application.service;

import com.nazgul.optimization.application.port.in.CreateContentChunkCommand;
import com.nazgul.optimization.application.port.in.CreateContentChunkUseCase;
import com.nazgul.optimization.application.port.in.CreateHobbyCardCommand;
import com.nazgul.optimization.application.port.in.CreateHobbyCardUseCase;
import com.nazgul.optimization.application.port.in.IngestHobbyContentCommand;
import com.nazgul.optimization.application.port.in.IngestHobbyContentResult;
import com.nazgul.optimization.application.port.in.IngestHobbyContentUseCase;
import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkItem;
import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkQuery;
import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkResult;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationCommand;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationUseCase;
import com.nazgul.optimization.application.port.in.RecommendPostsQuery;
import com.nazgul.optimization.application.port.in.RecommendPostsResult;
import com.nazgul.optimization.application.port.in.RecommendPostsUseCase;
import com.nazgul.optimization.application.port.in.RecordRecommendationTraceCommand;
import com.nazgul.optimization.application.port.in.RecordRecommendationTraceUseCase;
import com.nazgul.optimization.application.port.in.RunHobbyRagBenchmarkUseCase;
import com.nazgul.optimization.application.port.in.SearchHobbyRagQuery;
import com.nazgul.optimization.application.port.in.SearchHobbyRagResult;
import com.nazgul.optimization.application.port.in.SearchHobbyRagUseCase;
import com.nazgul.optimization.application.port.out.ContentChunkStore;
import com.nazgul.optimization.application.port.out.HobbyCardRelationStore;
import com.nazgul.optimization.application.port.out.HobbyRagRerankPort;
import com.nazgul.optimization.application.port.out.HobbyCardStore;
import com.nazgul.optimization.application.port.out.HobbyRagSummaryPort;
import com.nazgul.optimization.application.port.out.RecommendationTraceStore;
import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import com.nazgul.optimization.domain.model.HobbyCardRelation;
import com.nazgul.optimization.domain.model.RecommendationTrace;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class HobbyRagApplicationService implements
        CreateContentChunkUseCase,
        CreateHobbyCardUseCase,
        LinkHobbyCardRelationUseCase,
        RecordRecommendationTraceUseCase,
        IngestHobbyContentUseCase,
        SearchHobbyRagUseCase,
        RecommendPostsUseCase,
        RunHobbyRagBenchmarkUseCase {

    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 20;
    private static final int CHUNK_TEXT_SIZE = 450;

    private final ContentChunkStore contentChunkStore;
    private final HobbyCardStore hobbyCardStore;
    private final HobbyCardRelationStore hobbyCardRelationStore;
    private final RecommendationTraceStore recommendationTraceStore;
    private final HobbyRagRerankPort hobbyRagRerankPort;
    private final HobbyRagSummaryPort hobbyRagSummaryPort;

    @Value("${app.rag.ollama.chat-model:qwen3:8b}")
    private String chatModel;

    @Value("${app.rag.ollama.embedding-model:nomic-embed-text:latest}")
    private String embeddingModel;

    @Override
    @Transactional
    public ContentChunk createChunk(CreateContentChunkCommand command) {
        validateChunkCommand(command);

        ContentChunk chunk = ContentChunk.builder()
                .id(UUID.randomUUID())
                .sourceType(normalizeSourceType(command.sourceType()))
                .sourceId(command.sourceId())
                .hobbyId(command.hobbyId())
                .chunkIndex(command.chunkIndex())
                .text(command.text().trim())
                .tokenCount(command.tokenCount() == null ? approximateTokenCount(command.text()) : command.tokenCount())
                .startOffset(command.startOffset())
                .endOffset(command.endOffset())
                .contentHash(command.contentHash())
                .updatedAt(LocalDateTime.now())
                .build();

        return contentChunkStore.save(chunk);
    }

    @Override
    @Transactional
    public HobbyCard createCard(CreateHobbyCardCommand command) {
        validateCardCommand(command);

        HobbyCard card = HobbyCard.builder()
                .id(UUID.randomUUID())
                .hobbyId(command.hobbyId())
                .sourceType(normalizeSourceType(command.sourceType()))
                .sourceId(command.sourceId())
                .primaryChunkId(command.primaryChunkId())
                .type(normalizeCardType(command.type()))
                .title(command.title().trim())
                .summary(summarizeForCard(command.summary(), command.content(), command.claims()))
                .content(normalizeNullable(command.content()))
                .claims(copyList(command.claims()))
                .tags(normalizeTags(command.tags()))
                .evidenceChunkIds(command.evidenceChunkIds() == null ? List.of() : List.copyOf(command.evidenceChunkIds()))
                .versionLabel(StringUtils.hasText(command.versionLabel()) ? command.versionLabel().trim() : "v1")
                .tokenSize(approximateTokenCount(command.summary() + " " + command.content()))
                .trustScore(0.5d)
                .scopeKey(normalizeNullable(command.scopeKey()))
                .updatedAt(LocalDateTime.now())
                .build();

        return hobbyCardStore.save(card);
    }

    @Override
    @Transactional
    public HobbyCardRelation link(LinkHobbyCardRelationCommand command) {
        if (command.sourceCardId() == null || command.targetCardId() == null) {
            throw new IllegalArgumentException("sourceCardId와 targetCardId는 필수입니다.");
        }

        HobbyCardRelation relation = HobbyCardRelation.builder()
                .id(UUID.randomUUID())
                .sourceCardId(command.sourceCardId())
                .targetCardId(command.targetCardId())
                .relationType(StringUtils.hasText(command.relationType()) ? command.relationType().trim() : "RELATED_TO")
                .weight(command.weight() == null ? 1.0d : command.weight())
                .evidenceChunkId(command.evidenceChunkId())
                .updatedAt(LocalDateTime.now())
                .build();

        return hobbyCardRelationStore.save(relation);
    }

    @Override
    @Transactional
    public RecommendationTrace recordTrace(RecordRecommendationTraceCommand command) {
        RecommendationTrace trace = RecommendationTrace.builder()
                .id(UUID.randomUUID())
                .userId(command.userId())
                .requestType(StringUtils.hasText(command.requestType()) ? command.requestType().trim() : "SEARCH")
                .query(normalizeNullable(command.query()))
                .contextPostId(command.contextPostId())
                .contextHobbyId(command.contextHobbyId())
                .selectedCardIds(command.selectedCardIds() == null ? List.of() : List.copyOf(command.selectedCardIds()))
                .selectedChunkIds(command.selectedChunkIds() == null ? List.of() : List.copyOf(command.selectedChunkIds()))
                .recommendedTargetIds(command.recommendedTargetIds() == null ? List.of() : List.copyOf(command.recommendedTargetIds()))
                .totalHits(command.totalHits())
                .latencyMs(command.latencyMs())
                .createdAt(LocalDateTime.now())
                .build();

        return recommendationTraceStore.save(trace);
    }

    @Override
    @Transactional
    public IngestHobbyContentResult ingest(IngestHobbyContentCommand command) {
        if (!StringUtils.hasText(command.content())) {
            throw new IllegalArgumentException("content는 비어 있을 수 없습니다.");
        }

        List<ContentChunk> createdChunks = new ArrayList<>();
        List<String> splitTexts = splitContent(command.content());
        int startOffset = 0;

        for (int index = 0; index < splitTexts.size(); index++) {
            String chunkText = splitTexts.get(index);
            int endOffset = Math.min(command.content().length(), startOffset + chunkText.length());
            createdChunks.add(createChunk(new CreateContentChunkCommand(
                    command.sourceType(),
                    command.sourceId(),
                    command.hobbyId(),
                    index,
                    chunkText,
                    approximateTokenCount(chunkText),
                    startOffset,
                    endOffset,
                    Integer.toHexString(chunkText.hashCode())
            )));
            startOffset = endOffset;
        }

        HobbyCard createdCard = createCard(new CreateHobbyCardCommand(
                command.hobbyId(),
                command.sourceType(),
                command.sourceId(),
                createdChunks.get(0).getId(),
                command.cardType(),
                StringUtils.hasText(command.title()) ? command.title() : defaultTitle(command.content()),
                summarizeForCard(null, command.content(), List.of()),
                command.content(),
                extractClaims(command.content()),
                normalizeTags(command.tags()),
                createdChunks.stream().map(ContentChunk::getId).toList(),
                "v1",
                command.scopeKey()
        ));

        return new IngestHobbyContentResult(createdCard, createdChunks);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchHobbyRagResult search(SearchHobbyRagQuery query) {
        SearchExecution execution = executeSearch(
                query.userId(),
                query.hobbyId(),
                query.contextPostId(),
                query.query(),
                normalizeLimit(query.limit()),
                "SEARCH"
        );

        return new SearchHobbyRagResult(
                execution.query(),
                execution.answer(),
                execution.recommendedPostIds(),
                execution.cards(),
                execution.chunks(),
                execution.traceId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendPostsResult recommend(RecommendPostsQuery query) {
        SearchExecution execution = executeSearch(
                query.userId(),
                query.hobbyId(),
                query.contextPostId(),
                query.query(),
                normalizeLimit(query.limit()),
                "RECOMMEND"
        );

        return new RecommendPostsResult(
                query.userId(),
                query.hobbyId(),
                execution.query(),
                execution.recommendedPostIds(),
                execution.cards(),
                execution.chunks()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public HobbyRagBenchmarkResult run(HobbyRagBenchmarkQuery query) {
        int iterations = Math.max(1, query.iterations());
        List<String> benchmarkQueries = benchmarkQueries(query.hobbyId());
        List<HobbyRagBenchmarkItem> items = new ArrayList<>();
        long totalLatencyMs = 0L;

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (String benchmarkQuery : benchmarkQueries) {
                long startedAt = System.currentTimeMillis();
                SearchHobbyRagResult result = search(new SearchHobbyRagQuery(
                        query.userId(),
                        query.hobbyId(),
                        null,
                        benchmarkQuery,
                        DEFAULT_LIMIT
                ));
                long latencyMs = System.currentTimeMillis() - startedAt;
                totalLatencyMs += latencyMs;
                items.add(new HobbyRagBenchmarkItem(
                        benchmarkQuery,
                        latencyMs,
                        result.cards().size(),
                        result.chunks().size(),
                        result.recommendedPostIds(),
                        preview(result.answer())
                ));
            }
        }

        return new HobbyRagBenchmarkResult(
                "ollama",
                chatModel,
                embeddingModel,
                iterations,
                totalLatencyMs,
                items.isEmpty() ? 0.0d : (double) totalLatencyMs / items.size(),
                items
        );
    }

    private SearchExecution executeSearch(
            Long userId,
            Long hobbyId,
            Long contextPostId,
            String rawQuery,
            int limit,
            String requestType
    ) {
        String query = normalizeQuery(rawQuery);
        long startedAt = System.currentTimeMillis();
        int cardCandidateLimit = Math.max(limit * 3, 10);
        int chunkCandidateLimit = Math.max(limit * 4, 12);

        List<HobbyCard> keywordCards = hobbyCardStore.search(query, hobbyId, cardCandidateLimit);
        if (keywordCards.isEmpty()) {
            keywordCards = hobbyCardStore.search("", hobbyId, cardCandidateLimit);
        }
        List<HobbyCard> seedCards = hobbyRagRerankPort.rerankCards(query, keywordCards, limit);
        List<HobbyCard> relatedCards = expandRelations(seedCards, cardCandidateLimit);
        List<HobbyCard> cards = hobbyRagRerankPort.rerankCards(query, mergeCards(seedCards, relatedCards, cardCandidateLimit), limit);
        List<Long> sourceIds = cards.stream()
                .map(HobbyCard::getSourceId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        List<ContentChunk> chunkCandidates = contentChunkStore.search(query, hobbyId, sourceIds, chunkCandidateLimit);
        if (chunkCandidates.isEmpty()) {
            chunkCandidates = contentChunkStore.search("", hobbyId, sourceIds, chunkCandidateLimit);
        }
        List<ContentChunk> chunks = hobbyRagRerankPort.rerankChunks(query, chunkCandidates, Math.max(limit * 2, 4));
        String answer = hobbyRagSummaryPort.summarize(query, hobbyId, cards, chunks);
        List<Long> recommendedPostIds = collectRecommendedPostIds(cards, chunks);
        long latencyMs = System.currentTimeMillis() - startedAt;

        RecommendationTrace trace = recordTrace(new RecordRecommendationTraceCommand(
                userId,
                requestType,
                query,
                contextPostId,
                hobbyId,
                cards.stream().map(HobbyCard::getId).toList(),
                chunks.stream().map(ContentChunk::getId).toList(),
                recommendedPostIds,
                cards.size() + chunks.size(),
                latencyMs
        ));

        return new SearchExecution(query, answer, recommendedPostIds, cards, chunks, trace.getId());
    }

    private List<HobbyCard> expandRelations(List<HobbyCard> seedCards, int limit) {
        if (seedCards.isEmpty()) {
            return List.of();
        }

        List<HobbyCardRelation> relations = hobbyCardRelationStore.findBySourceCardIds(
                seedCards.stream().map(HobbyCard::getId).toList(),
                Math.max(limit * 2, 4)
        );

        Set<UUID> targetIds = new LinkedHashSet<>();
        for (HobbyCardRelation relation : relations) {
            targetIds.add(relation.getTargetCardId());
            if (targetIds.size() >= limit) {
                break;
            }
        }

        return hobbyCardStore.findByIds(targetIds);
    }

    private List<HobbyCard> mergeCards(List<HobbyCard> seedCards, List<HobbyCard> relatedCards, int limit) {
        List<HobbyCard> merged = new ArrayList<>();
        Set<UUID> seen = new LinkedHashSet<>();

        for (HobbyCard card : seedCards) {
            if (seen.add(card.getId())) {
                merged.add(card);
            }
        }

        for (HobbyCard card : relatedCards) {
            if (seen.add(card.getId())) {
                merged.add(card);
            }
            if (merged.size() >= limit) {
                break;
            }
        }

        return merged;
    }

    private List<Long> collectRecommendedPostIds(List<HobbyCard> cards, List<ContentChunk> chunks) {
        Set<Long> recommendedPostIds = new LinkedHashSet<>();

        for (HobbyCard card : cards) {
            if ("POST".equalsIgnoreCase(card.getSourceType()) && card.getSourceId() != null) {
                recommendedPostIds.add(card.getSourceId());
            }
        }

        for (ContentChunk chunk : chunks) {
            if ("POST".equalsIgnoreCase(chunk.getSourceType()) && chunk.getSourceId() != null) {
                recommendedPostIds.add(chunk.getSourceId());
            }
        }

        return recommendedPostIds.stream().toList();
    }

    private void validateChunkCommand(CreateContentChunkCommand command) {
        if (!StringUtils.hasText(command.sourceType())) {
            throw new IllegalArgumentException("sourceType은 필수입니다.");
        }
        if (command.sourceId() == null) {
            throw new IllegalArgumentException("sourceId는 필수입니다.");
        }
        if (!StringUtils.hasText(command.text())) {
            throw new IllegalArgumentException("chunk text는 비어 있을 수 없습니다.");
        }
    }

    private void validateCardCommand(CreateHobbyCardCommand command) {
        if (command.hobbyId() == null) {
            throw new IllegalArgumentException("hobbyId는 필수입니다.");
        }
        if (!StringUtils.hasText(command.sourceType())) {
            throw new IllegalArgumentException("sourceType은 필수입니다.");
        }
        if (command.sourceId() == null) {
            throw new IllegalArgumentException("sourceId는 필수입니다.");
        }
        if (command.primaryChunkId() == null) {
            throw new IllegalArgumentException("primaryChunkId는 필수입니다.");
        }
        if (!StringUtils.hasText(command.title())) {
            throw new IllegalArgumentException("title은 비어 있을 수 없습니다.");
        }
    }

    private String normalizeQuery(String query) {
        if (!StringUtils.hasText(query)) {
            throw new IllegalArgumentException("query는 비어 있을 수 없습니다.");
        }
        return query.trim();
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }

    private List<String> splitContent(String content) {
        String normalized = content.trim();
        if (normalized.length() <= CHUNK_TEXT_SIZE) {
            return List.of(normalized);
        }

        List<String> chunks = new ArrayList<>();
        int index = 0;
        while (index < normalized.length()) {
            int end = Math.min(normalized.length(), index + CHUNK_TEXT_SIZE);
            chunks.add(normalized.substring(index, end).trim());
            index = end;
        }
        return chunks;
    }

    private List<String> extractClaims(String content) {
        return java.util.Arrays.stream(content.split("[.!?\\n]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .limit(3)
                .toList();
    }

    private String summarizeForCard(String summary, String content, List<String> claims) {
        if (StringUtils.hasText(summary)) {
            return summary.trim();
        }

        if (claims != null && !claims.isEmpty() && StringUtils.hasText(claims.get(0))) {
            return claims.get(0);
        }

        if (!StringUtils.hasText(content)) {
            return "요약 없음";
        }

        String normalized = content.trim();
        return normalized.length() > 160 ? normalized.substring(0, 160) + "..." : normalized;
    }

    private List<String> normalizeTags(List<String> tags) {
        if (tags == null) {
            return List.of();
        }

        return tags.stream()
                .filter(StringUtils::hasText)
                .map(tag -> tag.trim().toLowerCase(Locale.ROOT))
                .distinct()
                .toList();
    }

    private String normalizeSourceType(String sourceType) {
        return sourceType.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeCardType(String type) {
        return StringUtils.hasText(type) ? type.trim().toUpperCase(Locale.ROOT) : "TOPIC";
    }

    private String normalizeNullable(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private List<String> copyList(List<String> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    private String defaultTitle(String content) {
        String normalized = content.trim();
        return normalized.length() > 40 ? normalized.substring(0, 40) + "..." : normalized;
    }

    private int approximateTokenCount(String text) {
        if (!StringUtils.hasText(text)) {
            return 0;
        }
        return Math.max(1, text.trim().split("\\s+").length);
    }

    private List<String> benchmarkQueries(Long hobbyId) {
        if (Long.valueOf(10L).equals(hobbyId)) {
            return List.of("러닝 초보 루틴", "러닝화 추천", "무릎 통증 줄이기");
        }
        if (Long.valueOf(20L).equals(hobbyId)) {
            return List.of("홈카페 원두 추천", "브루잉 비율", "그라인더 업그레이드");
        }
        if (Long.valueOf(30L).equals(hobbyId)) {
            return List.of("초보 캠핑 장비", "캠핑 조리 동선", "우천 캠핑");
        }
        return List.of("러닝 초보 루틴", "홈카페 원두 추천", "초보 캠핑 장비");
    }

    private String preview(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        String normalized = value.trim();
        return normalized.length() > 160 ? normalized.substring(0, 160) + "..." : normalized;
    }

    private record SearchExecution(
            String query,
            String answer,
            List<Long> recommendedPostIds,
            List<HobbyCard> cards,
            List<ContentChunk> chunks,
            UUID traceId
    ) {
    }
}
