package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;

public record IngestHobbyContentResult(
        HobbyCard card,
        List<ContentChunk> chunks
) {
}
