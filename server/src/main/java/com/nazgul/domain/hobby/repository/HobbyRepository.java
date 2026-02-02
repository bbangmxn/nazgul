package com.nazgul.domain.hobby.repository;

import com.nazgul.domain.hobby.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {
    
    Optional<Hobby> findByName(String name);
    
    List<Hobby> findByCategory(String category);
    
    List<Hobby> findByNameContainingIgnoreCase(String keyword);
    
    boolean existsByName(String name);
}
