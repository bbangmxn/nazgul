package com.nazgul.optimization.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HobbyCard {

    private UUID id;
    private Long hobbyId;
    private String sourceType;
    private Long sourceId;
    private UUID primaryChunkId;
    private String type;
    private String title;
    private String summary;
    private String content;
    private List<String> claims;
    private List<String> tags;
    private List<UUID> evidenceChunkIds;
    private String versionLabel;
    private Integer tokenSize;
    private Double trustScore;
    private String scopeKey;
    private LocalDateTime updatedAt;
}
