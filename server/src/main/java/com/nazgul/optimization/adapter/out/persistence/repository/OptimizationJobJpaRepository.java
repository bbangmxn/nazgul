package com.nazgul.optimization.adapter.out.persistence.repository;

import com.nazgul.optimization.adapter.out.persistence.entity.OptimizationJobEntity;
import com.nazgul.optimization.domain.model.OptimizationJobStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptimizationJobJpaRepository extends JpaRepository<OptimizationJobEntity, UUID> {

    List<OptimizationJobEntity> findByStatusOrderByCreatedAtAsc(OptimizationJobStatus status, Pageable pageable);
}
