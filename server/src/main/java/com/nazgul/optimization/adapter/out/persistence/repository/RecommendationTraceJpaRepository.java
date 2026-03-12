package com.nazgul.optimization.adapter.out.persistence.repository;

import com.nazgul.optimization.adapter.out.persistence.entity.RecommendationTraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationTraceJpaRepository extends JpaRepository<RecommendationTraceEntity, String> {
}
