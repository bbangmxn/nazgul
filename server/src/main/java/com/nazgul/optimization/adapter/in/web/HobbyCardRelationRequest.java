package com.nazgul.optimization.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record HobbyCardRelationRequest(
        @NotNull UUID sourceCardId,
        @NotNull UUID targetCardId,
        @NotBlank String relationType,
        Double weight,
        UUID evidenceChunkId
) {
}
