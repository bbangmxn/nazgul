package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.ContentChunk;
import java.util.Collection;
import java.util.List;

public interface ContentChunkStore {

    ContentChunk save(ContentChunk chunk);

    List<ContentChunk> search(String query, Long hobbyId, Collection<Long> sourceIds, int limit);
}
