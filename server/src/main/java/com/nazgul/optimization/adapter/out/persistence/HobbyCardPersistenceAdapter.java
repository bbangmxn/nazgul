package com.nazgul.optimization.adapter.out.persistence;

import com.nazgul.optimization.adapter.out.persistence.entity.HobbyCardEntity;
import com.nazgul.optimization.adapter.out.persistence.mapper.HobbyCardPersistenceMapper;
import com.nazgul.optimization.adapter.out.persistence.repository.HobbyCardJpaRepository;
import com.nazgul.optimization.application.port.out.HobbyCardStore;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class HobbyCardPersistenceAdapter implements HobbyCardStore {

    private final HobbyCardJpaRepository hobbyCardJpaRepository;
    private final HobbyCardPersistenceMapper hobbyCardPersistenceMapper;

    @Override
    public HobbyCard save(HobbyCard card) {
        return hobbyCardPersistenceMapper.toDomain(
                hobbyCardJpaRepository.save(hobbyCardPersistenceMapper.toEntity(card))
        );
    }

    @Override
    public Optional<HobbyCard> findById(UUID cardId) {
        return hobbyCardJpaRepository.findById(cardId.toString()).map(hobbyCardPersistenceMapper::toDomain);
    }

    @Override
    public List<HobbyCard> findByIds(Collection<UUID> cardIds) {
        if (cardIds == null || cardIds.isEmpty()) {
            return List.of();
        }
        return hobbyCardJpaRepository.findByIdIn(cardIds.stream().map(UUID::toString).toList()).stream()
                .map(hobbyCardPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<HobbyCard> search(String query, Long hobbyId, int limit) {
        String normalizedQuery = query == null ? "" : query.toLowerCase();
        List<HobbyCardEntity> candidates = hobbyId == null
                ? hobbyCardJpaRepository.findAllByOrderByUpdatedAtDesc()
                : hobbyCardJpaRepository.findByHobbyIdOrderByUpdatedAtDesc(hobbyId);

        return candidates.stream()
                .map(hobbyCardPersistenceMapper::toDomain)
                .filter(card -> normalizedQuery.isBlank() || score(card, normalizedQuery) > 0)
                .sorted(Comparator
                        .comparingInt((HobbyCard card) -> score(card, normalizedQuery)).reversed()
                        .thenComparing(HobbyCard::getTrustScore, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(HobbyCard::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }

    private int score(HobbyCard card, String normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            return 0;
        }

        int score = 0;
        score += keywordScore(card.getTitle(), normalizedQuery) * 3;
        score += keywordScore(card.getSummary(), normalizedQuery) * 2;
        score += keywordScore(card.getContent(), normalizedQuery);

        if (card.getClaims() != null) {
            score += card.getClaims().stream().mapToInt(claim -> keywordScore(claim, normalizedQuery)).sum();
        }
        if (card.getTags() != null) {
            score += card.getTags().stream().mapToInt(tag -> keywordScore(tag, normalizedQuery) * 2).sum();
        }
        return score;
    }

    private int keywordScore(String text, String normalizedQuery) {
        if (text == null || normalizedQuery.isBlank()) {
            return 0;
        }
        String normalizedText = text.toLowerCase();
        int score = 0;
        for (String token : normalizedQuery.split("\\s+")) {
            if (!token.isBlank() && normalizedText.contains(token)) {
                score += 1;
            }
        }
        return score;
    }
}
