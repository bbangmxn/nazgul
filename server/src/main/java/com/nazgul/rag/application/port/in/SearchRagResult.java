package com.nazgul.rag.application.port.in;

import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.vo.RetrievalScope;
import java.util.List;

public record SearchRagResult(
        String query,
        RetrievalScope scope,
        List<RagCard> cards,
        List<RagChunk> chunks
) {
}
