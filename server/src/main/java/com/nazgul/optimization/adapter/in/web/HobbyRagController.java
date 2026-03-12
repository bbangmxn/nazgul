package com.nazgul.optimization.adapter.in.web;

import com.nazgul.optimization.application.port.in.IngestHobbyContentCommand;
import com.nazgul.optimization.application.port.in.IngestHobbyContentUseCase;
import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkQuery;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationCommand;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationUseCase;
import com.nazgul.optimization.application.port.in.RecommendPostsQuery;
import com.nazgul.optimization.application.port.in.RecommendPostsUseCase;
import com.nazgul.optimization.application.port.in.RunHobbyRagBenchmarkUseCase;
import com.nazgul.optimization.application.port.in.SearchHobbyRagQuery;
import com.nazgul.optimization.application.port.in.SearchHobbyRagUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/optimization/rag")
public class HobbyRagController {

    private final IngestHobbyContentUseCase ingestHobbyContentUseCase;
    private final LinkHobbyCardRelationUseCase linkHobbyCardRelationUseCase;
    private final SearchHobbyRagUseCase searchHobbyRagUseCase;
    private final RecommendPostsUseCase recommendPostsUseCase;
    private final RunHobbyRagBenchmarkUseCase runHobbyRagBenchmarkUseCase;

    @PostMapping("/ingest")
    public HobbyRagIngestResponse ingest(@Valid @RequestBody HobbyRagIngestRequest request) {
        return HobbyRagIngestResponse.from(ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                request.sourceType(),
                request.sourceId(),
                request.hobbyId(),
                request.cardType(),
                request.title(),
                request.content(),
                request.tags(),
                request.scopeKey()
        )));
    }

    @PostMapping("/relations")
    public void linkRelation(@Valid @RequestBody HobbyCardRelationRequest request) {
        linkHobbyCardRelationUseCase.link(new LinkHobbyCardRelationCommand(
                request.sourceCardId(),
                request.targetCardId(),
                request.relationType(),
                request.weight(),
                request.evidenceChunkId()
        ));
    }

    @GetMapping("/search")
    public HobbyRagSearchResponse search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long hobbyId,
            @RequestParam(required = false) Long contextPostId,
            @RequestParam String query,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        return HobbyRagSearchResponse.from(searchHobbyRagUseCase.search(
                new SearchHobbyRagQuery(userId, hobbyId, contextPostId, query, limit)
        ));
    }

    @GetMapping("/recommend")
    public HobbyRagRecommendResponse recommend(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long hobbyId,
            @RequestParam(required = false) Long contextPostId,
            @RequestParam String query,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        return HobbyRagRecommendResponse.from(recommendPostsUseCase.recommend(
                new RecommendPostsQuery(userId, hobbyId, query, contextPostId, limit)
        ));
    }

    @GetMapping("/benchmark")
    public HobbyRagBenchmarkResponse benchmark(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long hobbyId,
            @RequestParam(defaultValue = "1") @Min(1) @Max(5) int iterations
    ) {
        return HobbyRagBenchmarkResponse.from(runHobbyRagBenchmarkUseCase.run(
                new HobbyRagBenchmarkQuery(userId, hobbyId, iterations)
        ));
    }
}
