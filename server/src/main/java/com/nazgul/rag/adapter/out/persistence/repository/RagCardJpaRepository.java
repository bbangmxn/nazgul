package com.nazgul.rag.adapter.out.persistence.repository;

import com.nazgul.rag.adapter.out.persistence.entity.RagCardJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RagCardJpaRepository extends JpaRepository<RagCardJpaEntity, UUID> {

    @Query("""
            select card
            from RagCardJpaEntity card
            where lower(card.title) like lower(concat('%', :query, '%'))
               or lower(card.summary) like lower(concat('%', :query, '%'))
            order by card.updatedAt desc
            """)
    List<RagCardJpaEntity> searchCards(@Param("query") String query, Pageable pageable);
}
