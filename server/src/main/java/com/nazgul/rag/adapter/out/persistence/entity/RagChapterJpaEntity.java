package com.nazgul.rag.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "rag_chapters")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagChapterJpaEntity {

    @Id
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chapter_index", nullable = false)
    private Integer chapterIndex;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "section_path", length = 500)
    private String sectionPath;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
