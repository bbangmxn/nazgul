package com.nazgul.rag.adapter.in.web;

import com.nazgul.rag.application.port.in.SearchRagQuery;
import com.nazgul.rag.application.port.in.SearchRagUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rag")
public class RagQueryController {

    private final SearchRagUseCase searchRagUseCase;

    @GetMapping("/search")
    public RagSearchResponse search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int cardLimit,
            @RequestParam(defaultValue = "6") @Min(1) @Max(20) int evidenceLimit
    ) {
        return RagSearchResponse.from(searchRagUseCase.search(new SearchRagQuery(query, cardLimit, evidenceLimit)));
    }
}
