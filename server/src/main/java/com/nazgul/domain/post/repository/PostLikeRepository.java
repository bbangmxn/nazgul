package com.nazgul.domain.post.repository;

import com.nazgul.domain.post.entity.Post;
import com.nazgul.domain.post.entity.PostLike;
import com.nazgul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    Optional<PostLike> findByUserAndPost(User user, Post post);
    
    boolean existsByUserAndPost(User user, Post post);
    
    void deleteByUserAndPost(User user, Post post);
    
    long countByPost(Post post);
}
