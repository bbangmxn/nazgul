package com.nazgul.rag.application.port.out;

import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.vo.RetrievalScope;
import java.util.List;

public interface LoadRagChunkPort {

    List<RagChunk> searchChunks(String query, RetrievalScope scope, int limit);
}
