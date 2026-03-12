package com.nazgul.optimization.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record HobbyRagIngestRequest(
        @NotBlank String sourceType,
        @NotNull Long sourceId,
        @NotNull Long hobbyId,
        String cardType,
        String title,
        @NotBlank String content,
        List<String> tags,
        String scopeKey
) {
}
