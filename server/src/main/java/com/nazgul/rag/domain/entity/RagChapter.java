package com.nazgul.rag.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RagChapter {

    private UUID id;
    private UUID documentId;
    private Integer chapterIndex;
    private String title;
    private String sectionPath;
    private LocalDateTime updatedAt;
}
