package com.nazgul.optimization.adapter.out.persistence.repository;

import com.nazgul.optimization.adapter.out.persistence.entity.HobbyCardEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyCardJpaRepository extends JpaRepository<HobbyCardEntity, String> {

    List<HobbyCardEntity> findAllByOrderByUpdatedAtDesc();

    List<HobbyCardEntity> findByHobbyIdOrderByUpdatedAtDesc(Long hobbyId);

    List<HobbyCardEntity> findByIdIn(Collection<String> ids);
}
