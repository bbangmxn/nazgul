package com.nazgul.rag.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvidenceRefEmbeddable {

    @Column(name = "chunk_id", nullable = false)
    private UUID chunkId;

    @Column(name = "locator", length = 255)
    private String locator;

    @Column(name = "priority")
    private Integer priority;
}
