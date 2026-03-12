package com.nazgul.rag.adapter.out.persistence;

import com.nazgul.rag.adapter.out.persistence.mapper.RagPersistenceMapper;
import com.nazgul.rag.adapter.out.persistence.repository.RagCardJpaRepository;
import com.nazgul.rag.adapter.out.persistence.repository.RagChunkJpaRepository;
import com.nazgul.rag.adapter.out.persistence.repository.RagQueryTraceJpaRepository;
import com.nazgul.rag.application.port.out.LoadRagCardPort;
import com.nazgul.rag.application.port.out.LoadRagChunkPort;
import com.nazgul.rag.application.port.out.SaveRagQueryTracePort;
import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.entity.RagQueryTrace;
import com.nazgul.rag.domain.vo.RetrievalScope;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RagPersistenceAdapter implements LoadRagCardPort, LoadRagChunkPort, SaveRagQueryTracePort {

    private final RagCardJpaRepository ragCardJpaRepository;
    private final RagChunkJpaRepository ragChunkJpaRepository;
    private final RagQueryTraceJpaRepository ragQueryTraceJpaRepository;
    private final RagPersistenceMapper ragPersistenceMapper;

    @Override
    public List<RagCard> searchCards(String query, int limit) {
        return ragCardJpaRepository.searchCards(query, PageRequest.of(0, limit))
                .stream()
                .map(ragPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<RagChunk> searchChunks(String query, RetrievalScope scope, int limit) {
        PageRequest pageable = PageRequest.of(0, limit);

        if (scope.isGlobal()) {
            return ragChunkJpaRepository.searchGlobal(query, pageable)
                    .stream()
                    .map(ragPersistenceMapper::toDomain)
                    .toList();
        }

        if (!scope.documentIds().isEmpty() && !scope.chapterIds().isEmpty()) {
            return ragChunkJpaRepository.searchByDocumentIdsOrChapterIds(query, scope.documentIds(), scope.chapterIds(), pageable)
                    .stream()
                    .map(ragPersistenceMapper::toDomain)
                    .toList();
        }

        if (!scope.documentIds().isEmpty()) {
            return ragChunkJpaRepository.searchByDocumentIds(query, scope.documentIds(), pageable)
                    .stream()
                    .map(ragPersistenceMapper::toDomain)
                    .toList();
        }

        return ragChunkJpaRepository.searchByChapterIds(query, scope.chapterIds(), pageable)
                .stream()
                .map(ragPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public RagQueryTrace save(RagQueryTrace trace) {
        ragQueryTraceJpaRepository.save(ragPersistenceMapper.toJpa(trace));
        return trace;
    }
}
