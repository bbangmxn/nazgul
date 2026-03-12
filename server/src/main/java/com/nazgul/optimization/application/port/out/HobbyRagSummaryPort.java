package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;

public interface HobbyRagSummaryPort {

    String summarize(String query, Long hobbyId, List<HobbyCard> cards, List<ContentChunk> chunks);
}
