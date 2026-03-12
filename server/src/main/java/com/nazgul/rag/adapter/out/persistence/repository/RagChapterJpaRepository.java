package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagChapterJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RagChapterJpaRepository extends JpaRepository<RagChapterJpaEntity, UUID> {
}
