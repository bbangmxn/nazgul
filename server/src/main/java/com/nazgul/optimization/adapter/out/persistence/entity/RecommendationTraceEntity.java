package com.nazgul.optimization.adapter.out.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recommendation_traces")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationTraceEntity {

    @Id
    @Column(length = 36)
    private String id;

    private Long userId;

    @Column(nullable = false, length = 30)
    private String requestType;

    @Column(length = 1000)
    private String query;

    private Long contextPostId;

    private Long contextHobbyId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recommendation_trace_cards", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "card_id", nullable = false, length = 36)
    @Builder.Default
    private List<String> selectedCardIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recommendation_trace_chunks", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "chunk_id", nullable = false, length = 36)
    @Builder.Default
    private List<String> selectedChunkIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recommendation_trace_targets", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "target_id", nullable = false)
    @Builder.Default
    private List<Long> recommendedTargetIds = new ArrayList<>();

    private Integer totalHits;

    private Long latencyMs;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
