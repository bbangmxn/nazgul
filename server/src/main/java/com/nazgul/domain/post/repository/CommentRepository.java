package com.nazgul.domain.post.repository;

import com.nazgul.domain.post.entity.Comment;
import com.nazgul.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByPostAndParentIsNull(Post post, Pageable pageable);
    
    List<Comment> findByParent(Comment parent);
    
    long countByPost(Post post);
}
