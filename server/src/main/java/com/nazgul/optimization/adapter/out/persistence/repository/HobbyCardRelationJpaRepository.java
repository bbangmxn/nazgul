package com.nazgul.optimization.adapter.out.persistence.repository;

import com.nazgul.optimization.adapter.out.persistence.entity.HobbyCardRelationEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyCardRelationJpaRepository extends JpaRepository<HobbyCardRelationEntity, String> {

    List<HobbyCardRelationEntity> findBySourceCardIdInOrderByWeightDescUpdatedAtDesc(Collection<String> sourceCardIds, Pageable pageable);
}
