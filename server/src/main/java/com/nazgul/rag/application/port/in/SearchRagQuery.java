package com.nazgul.rag.application.port.in;

public record SearchRagQuery(
        String query,
        int cardLimit,
        int evidenceLimit
) {
}
