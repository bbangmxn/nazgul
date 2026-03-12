package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagQueryTraceJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RagQueryTraceJpaRepository extends JpaRepository<RagQueryTraceJpaEntity, UUID> {
}
