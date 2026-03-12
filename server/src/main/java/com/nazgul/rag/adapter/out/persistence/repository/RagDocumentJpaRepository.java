package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagDocumentJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RagDocumentJpaRepository extends JpaRepository<RagDocumentJpaEntity, UUID> {
}
