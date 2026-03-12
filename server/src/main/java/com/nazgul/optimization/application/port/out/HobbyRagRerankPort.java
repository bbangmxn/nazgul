package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;

public interface HobbyRagRerankPort {

    List<HobbyCard> rerankCards(String query, List<HobbyCard> candidates, int limit);

    List<ContentChunk> rerankChunks(String query, List<ContentChunk> candidates, int limit);
}
