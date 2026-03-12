package com.nazgul.rag.domain.vo;

import java.util.UUID;

public record EvidenceRef(
        UUID chunkId,
        String locator,
        Integer priority
) {

    public EvidenceRef {
        if (chunkId == null) {
            throw new IllegalArgumentException("evidence chunkId는 필수입니다.");
        }
    }
}
