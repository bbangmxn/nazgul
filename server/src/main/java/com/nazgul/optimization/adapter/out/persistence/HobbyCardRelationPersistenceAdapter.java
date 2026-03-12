package com.nazgul.optimization.adapter.out.persistence;

import com.nazgul.optimization.adapter.out.persistence.mapper.HobbyCardRelationPersistenceMapper;
import com.nazgul.optimization.adapter.out.persistence.repository.HobbyCardRelationJpaRepository;
import com.nazgul.optimization.application.port.out.HobbyCardRelationStore;
import com.nazgul.optimization.domain.model.HobbyCardRelation;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class HobbyCardRelationPersistenceAdapter implements HobbyCardRelationStore {

    private final HobbyCardRelationJpaRepository hobbyCardRelationJpaRepository;
    private final HobbyCardRelationPersistenceMapper hobbyCardRelationPersistenceMapper;

    @Override
    public HobbyCardRelation save(HobbyCardRelation relation) {
        return hobbyCardRelationPersistenceMapper.toDomain(
                hobbyCardRelationJpaRepository.save(hobbyCardRelationPersistenceMapper.toEntity(relation))
        );
    }

    @Override
    public List<HobbyCardRelation> findBySourceCardIds(Collection<UUID> sourceCardIds, int limit) {
        if (sourceCardIds == null || sourceCardIds.isEmpty()) {
            return List.of();
        }
        return hobbyCardRelationJpaRepository.findBySourceCardIdInOrderByWeightDescUpdatedAtDesc(
                        sourceCardIds.stream().map(UUID::toString).toList(),
                        PageRequest.of(0, limit)
                ).stream()
                .map(hobbyCardRelationPersistenceMapper::toDomain)
                .toList();
    }
}
