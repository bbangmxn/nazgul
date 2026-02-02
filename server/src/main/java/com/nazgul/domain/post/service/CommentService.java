package com.nazgul.domain.post.service;

import com.nazgul.domain.post.dto.CommentDto;
import com.nazgul.domain.post.entity.Comment;
import com.nazgul.domain.post.entity.Post;
import com.nazgul.domain.post.repository.CommentRepository;
import com.nazgul.domain.post.repository.PostRepository;
import com.nazgul.domain.user.entity.User;
import com.nazgul.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto.CommentResponse createComment(Long postId, Long userId, CommentDto.CreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        }

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(request.getContent())
                .parent(parent)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.CommentResponse.from(savedComment, List.of());
    }

    public Page<CommentDto.CommentResponse> getComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        Page<Comment> comments = commentRepository.findByPostAndParentIsNull(post, pageable);

        return comments.map(comment -> {
            List<CommentDto.CommentResponse> replies = commentRepository.findByParent(comment).stream()
                    .map(reply -> CommentDto.CommentResponse.from(reply, List.of()))
                    .collect(Collectors.toList());
            return CommentDto.CommentResponse.from(comment, replies);
        });
    }

    @Transactional
    public CommentDto.CommentResponse updateComment(Long commentId, Long userId, CommentDto.UpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());

        List<CommentDto.CommentResponse> replies = commentRepository.findByParent(comment).stream()
                .map(reply -> CommentDto.CommentResponse.from(reply, List.of()))
                .collect(Collectors.toList());

        return CommentDto.CommentResponse.from(comment, replies);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
