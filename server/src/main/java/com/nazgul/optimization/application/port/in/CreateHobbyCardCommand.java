package com.nazgul.optimization.application.port.in;

import java.util.List;
import java.util.UUID;

public record CreateHobbyCardCommand(
        Long hobbyId,
        String sourceType,
        Long sourceId,
        UUID primaryChunkId,
        String type,
        String title,
        String summary,
        String content,
        List<String> claims,
        List<String> tags,
        List<UUID> evidenceChunkIds,
        String versionLabel,
        String scopeKey
) {
}
