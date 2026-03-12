package com.nazgul.optimization.adapter.out.persistence.entity;

import com.nazgul.optimization.domain.model.OptimizationJobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "optimization_jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long requestedByUserId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String objective;

    @Column(length = 4000)
    private String inputSummary;

    @Lob
    private String inputPayload;

    @Column(nullable = false)
    private boolean aiAnalysisEnabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OptimizationJobStatus status;

    @Lob
    private String aiAnalysis;

    @Lob
    private String solutionSummary;

    @Lob
    private String failureReason;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
