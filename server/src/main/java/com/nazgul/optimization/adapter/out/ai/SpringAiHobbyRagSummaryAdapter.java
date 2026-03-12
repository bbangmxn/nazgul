package com.nazgul.optimization.adapter.out.ai;

import com.nazgul.optimization.application.port.out.HobbyRagSummaryPort;
import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class SpringAiHobbyRagSummaryAdapter implements HobbyRagSummaryPort {

    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;

    @Value("${spring.ai.openai.api-key:}")
    private String openAiApiKey;

    @Override
    public String summarize(String query, Long hobbyId, List<HobbyCard> cards, List<ContentChunk> chunks) {
        if (!StringUtils.hasText(openAiApiKey)) {
            return fallbackSummary(query, cards, chunks);
        }

        ChatClient.Builder builder = chatClientBuilderProvider.getIfAvailable();
        if (builder == null) {
            return fallbackSummary(query, cards, chunks);
        }

        String prompt = """
                너는 취미 기반 SNS의 RAG 검색 어시스턴트다.
                사용자의 질문에 대해 아래 근거만 사용해서 짧고 정확하게 요약해라.
                질문: %s
                hobbyId: %s

                카드:
                %s

                근거 청크:
                %s

                규칙:
                1. 5문장 이내로 답해라.
                2. 추천 포인트와 근거를 함께 설명해라.
                3. 근거가 부족하면 부족하다고 명시해라.
                """.formatted(
                safe(query),
                hobbyId == null ? "N/A" : hobbyId,
                renderCards(cards),
                renderChunks(chunks)
        );

        String content = builder.build()
                .prompt()
                .user(prompt)
                .call()
                .content();

        return StringUtils.hasText(content) ? content : fallbackSummary(query, cards, chunks);
    }

    private String fallbackSummary(String query, List<HobbyCard> cards, List<ContentChunk> chunks) {
        String cardTitles = cards.stream()
                .limit(3)
                .map(HobbyCard::getTitle)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(", "));

        String chunkPreview = chunks.stream()
                .limit(2)
                .map(ContentChunk::getText)
                .filter(StringUtils::hasText)
                .map(text -> text.length() > 120 ? text.substring(0, 120) + "..." : text)
                .collect(Collectors.joining(" / "));

        if (!StringUtils.hasText(cardTitles) && !StringUtils.hasText(chunkPreview)) {
            return "검색 결과가 충분하지 않아 요약할 수 없습니다.";
        }

        return """
                질문 '%s' 기준으로 관련 카드와 근거를 찾았습니다.
                핵심 카드: %s
                근거 요약: %s
                """.formatted(
                safe(query),
                StringUtils.hasText(cardTitles) ? cardTitles : "없음",
                StringUtils.hasText(chunkPreview) ? chunkPreview : "근거 청크 없음"
        ).trim();
    }

    private String renderCards(List<HobbyCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return "- 없음";
        }

        return cards.stream()
                .limit(5)
                .map(card -> "- %s | %s | tags=%s".formatted(
                        safe(card.getTitle()),
                        safe(card.getSummary()),
                        card.getTags() == null ? List.of() : card.getTags()))
                .collect(Collectors.joining("\n"));
    }

    private String renderChunks(List<ContentChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return "- 없음";
        }

        return chunks.stream()
                .limit(5)
                .map(chunk -> "- [%s:%s] %s".formatted(
                        safe(chunk.getSourceType()),
                        chunk.getSourceId(),
                        safe(chunk.getText())))
                .collect(Collectors.joining("\n"));
    }

    private String safe(String value) {
        return StringUtils.hasText(value) ? value : "N/A";
    }
}
