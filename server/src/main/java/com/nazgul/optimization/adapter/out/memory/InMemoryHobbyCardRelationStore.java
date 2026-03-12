package com.nazgul.optimization.adapter.out.memory;

import com.nazgul.optimization.application.port.out.HobbyCardRelationStore;
import com.nazgul.optimization.domain.model.HobbyCardRelation;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class InMemoryHobbyCardRelationStore implements HobbyCardRelationStore {

    private final Map<UUID, HobbyCardRelation> storage = new ConcurrentHashMap<>();

    @Override
    public HobbyCardRelation save(HobbyCardRelation relation) {
        storage.put(relation.getId(), relation);
        return relation;
    }

    @Override
    public List<HobbyCardRelation> findBySourceCardIds(Collection<UUID> sourceCardIds, int limit) {
        if (sourceCardIds == null || sourceCardIds.isEmpty()) {
            return List.of();
        }

        return storage.values().stream()
                .filter(relation -> sourceCardIds.contains(relation.getSourceCardId()))
                .sorted(Comparator
                        .comparing(HobbyCardRelation::getWeight, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(HobbyCardRelation::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }
}
