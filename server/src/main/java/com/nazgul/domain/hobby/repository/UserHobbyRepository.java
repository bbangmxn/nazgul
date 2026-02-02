package com.nazgul.domain.hobby.repository;

import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.hobby.entity.UserHobby;
import com.nazgul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {
    
    List<UserHobby> findByUser(User user);
    
    List<UserHobby> findByHobby(Hobby hobby);
    
    Optional<UserHobby> findByUserAndHobby(User user, Hobby hobby);
    
    boolean existsByUserAndHobby(User user, Hobby hobby);
    
    void deleteByUserAndHobby(User user, Hobby hobby);
    
    @Query("SELECT uh.hobby.id FROM UserHobby uh WHERE uh.user.id = :userId")
    List<Long> findHobbyIdsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(uh) FROM UserHobby uh WHERE uh.hobby.id = :hobbyId")
    long countByHobbyId(@Param("hobbyId") Long hobbyId);
}
