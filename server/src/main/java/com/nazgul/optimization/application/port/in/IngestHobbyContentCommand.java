package com.nazgul.optimization.application.port.in;

import java.util.List;

public record IngestHobbyContentCommand(
        String sourceType,
        Long sourceId,
        Long hobbyId,
        String cardType,
        String title,
        String content,
        List<String> tags,
        String scopeKey
) {
}
