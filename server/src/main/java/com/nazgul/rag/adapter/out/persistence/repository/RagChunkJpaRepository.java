package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagChunkJpaEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RagChunkJpaRepository extends JpaRepository<RagChunkJpaEntity, UUID> {

    @Query("""
            select chunk
            from RagChunkJpaEntity chunk
            where lower(chunk.text) like lower(concat('%', :query, '%'))
            order by chunk.updatedAt desc, chunk.chunkIndex asc
            """)
    List<RagChunkJpaEntity> searchGlobal(@Param("query") String query, Pageable pageable);

    @Query("""
            select chunk
            from RagChunkJpaEntity chunk
            where chunk.documentId in :documentIds
              and lower(chunk.text) like lower(concat('%', :query, '%'))
            order by chunk.updatedAt desc, chunk.chunkIndex asc
            """)
    List<RagChunkJpaEntity> searchByDocumentIds(
            @Param("query") String query,
            @Param("documentIds") Collection<UUID> documentIds,
            Pageable pageable
    );

    @Query("""
            select chunk
            from RagChunkJpaEntity chunk
            where chunk.chapterId in :chapterIds
              and lower(chunk.text) like lower(concat('%', :query, '%'))
            order by chunk.updatedAt desc, chunk.chunkIndex asc
            """)
    List<RagChunkJpaEntity> searchByChapterIds(
            @Param("query") String query,
            @Param("chapterIds") Collection<UUID> chapterIds,
            Pageable pageable
    );

    @Query("""
            select chunk
            from RagChunkJpaEntity chunk
            where (chunk.documentId in :documentIds or chunk.chapterId in :chapterIds)
              and lower(chunk.text) like lower(concat('%', :query, '%'))
            order by chunk.updatedAt desc, chunk.chunkIndex asc
            """)
    List<RagChunkJpaEntity> searchByDocumentIdsOrChapterIds(
            @Param("query") String query,
            @Param("documentIds") Collection<UUID> documentIds,
            @Param("chapterIds") Collection<UUID> chapterIds,
            Pageable pageable
    );
}
