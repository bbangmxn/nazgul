package com.nazgul.domain.user.repository;

import com.nazgul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByNickname(String nickname);
    
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.hobbies uh " +
           "WHERE uh.hobby.id IN :hobbyIds " +
           "AND u.id != :userId")
    List<User> findByHobbiesIn(@Param("hobbyIds") List<Long> hobbyIds, @Param("userId") Long userId);
    
    @Query("SELECT u FROM User u WHERE u.nickname LIKE %:keyword% OR u.bio LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
