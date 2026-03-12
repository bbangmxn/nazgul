package com.nazgul.optimization.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "content_chunks",
        indexes = {
                @Index(name = "idx_content_chunks_hobby_updated", columnList = "hobbyId, updatedAt"),
                @Index(name = "idx_content_chunks_source", columnList = "sourceType, sourceId")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentChunkEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 30)
    private String sourceType;

    @Column(nullable = false)
    private Long sourceId;

    private Long hobbyId;

    @Column(nullable = false)
    private Integer chunkIndex;

    @Lob
    @Column(nullable = false)
    private String text;

    private Integer tokenCount;

    private Integer startOffset;

    private Integer endOffset;

    @Column(length = 128)
    private String contentHash;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
