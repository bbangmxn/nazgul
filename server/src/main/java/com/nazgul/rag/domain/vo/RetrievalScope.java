package com.nazgul.rag.domain.vo;

import java.util.Set;
import java.util.UUID;

public record RetrievalScope(
        Set<UUID> documentIds,
        Set<UUID> chapterIds
) {

    public RetrievalScope {
        documentIds = documentIds == null ? Set.of() : Set.copyOf(documentIds);
        chapterIds = chapterIds == null ? Set.of() : Set.copyOf(chapterIds);
    }

    public static RetrievalScope global() {
        return new RetrievalScope(Set.of(), Set.of());
    }

    public boolean isGlobal() {
        return documentIds.isEmpty() && chapterIds.isEmpty();
    }
}
