package com.nazgul.rag.domain.entity;

import com.nazgul.rag.domain.enums.RagCardType;
import com.nazgul.rag.domain.vo.EvidenceRef;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RagCard {

    private UUID id;
    private UUID documentId;
    private UUID chapterId;
    private UUID primaryChunkId;
    private RagCardType type;
    private String title;
    private String summary;
    private List<String> claims;
    private List<String> tags;
    private List<EvidenceRef> evidenceRefs;
    private String versionLabel;
    private Double trustScore;
    private LocalDateTime updatedAt;
}
