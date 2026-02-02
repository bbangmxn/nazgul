package com.nazgul.domain.post.repository;

import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.post.entity.Post;
import com.nazgul.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByAuthor(User author, Pageable pageable);
    
    Page<Post> findByHobby(Hobby hobby, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.author.id IN :userIds ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorIds(@Param("userIds") List<Long> userIds, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.hobby.id IN :hobbyIds ORDER BY p.createdAt DESC")
    Page<Post> findByHobbyIds(@Param("hobbyIds") List<Long> hobbyIds, Pageable pageable);
    
    @Query("SELECT p FROM Post p ORDER BY SIZE(p.likes) DESC, p.createdAt DESC")
    Page<Post> findPopularPosts(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword%")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
