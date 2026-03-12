package com.nazgul.optimization.adapter.out.persistence.repository;

import com.nazgul.optimization.adapter.out.persistence.entity.ContentChunkEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentChunkJpaRepository extends JpaRepository<ContentChunkEntity, String> {

    List<ContentChunkEntity> findAllByOrderByUpdatedAtDesc();

    List<ContentChunkEntity> findByHobbyIdOrderByUpdatedAtDesc(Long hobbyId);

    List<ContentChunkEntity> findBySourceIdInOrderByUpdatedAtDesc(Collection<Long> sourceIds);

    List<ContentChunkEntity> findByHobbyIdAndSourceIdInOrderByUpdatedAtDesc(Long hobbyId, Collection<Long> sourceIds);
}
