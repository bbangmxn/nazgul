package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagCardRelationJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RagCardRelationJpaRepository extends JpaRepository<RagCardRelationJpaEntity, UUID> {
}
