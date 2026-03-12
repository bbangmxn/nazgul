package com.nazgul.optimization.application.port.in;

import java.util.UUID;

public record LinkHobbyCardRelationCommand(
        UUID sourceCardId,
        UUID targetCardId,
        String relationType,
        Double weight,
        UUID evidenceChunkId
) {
}
