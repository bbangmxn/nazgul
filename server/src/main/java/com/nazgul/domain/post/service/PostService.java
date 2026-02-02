package com.nazgul.domain.post.service;

import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.hobby.repository.HobbyRepository;
import com.nazgul.domain.hobby.repository.UserHobbyRepository;
import com.nazgul.domain.post.dto.PostDto;
import com.nazgul.domain.post.entity.Post;
import com.nazgul.domain.post.entity.PostLike;
import com.nazgul.domain.post.repository.CommentRepository;
import com.nazgul.domain.post.repository.PostLikeRepository;
import com.nazgul.domain.post.repository.PostRepository;
import com.nazgul.domain.user.entity.Follow;
import com.nazgul.domain.user.entity.User;
import com.nazgul.domain.user.repository.FollowRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final HobbyRepository hobbyRepository;
    private final FollowRepository followRepository;
    private final UserHobbyRepository userHobbyRepository;

    @Transactional
    public PostDto.PostResponse createPost(Long userId, PostDto.CreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Hobby hobby = null;
        if (request.getHobbyId() != null) {
            hobby = hobbyRepository.findById(request.getHobbyId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 취미입니다."));
        }

        Post post = Post.builder()
                .author(author)
                .content(request.getContent())
                .hobby(hobby)
                .imageUrl(request.getImageUrl())
                .build();

        Post savedPost = postRepository.save(post);
        return PostDto.PostResponse.from(savedPost, 0, 0, false);
    }

    public PostDto.PostResponse getPost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        long likeCount = postLikeRepository.countByPost(post);
        long commentCount = commentRepository.countByPost(post);
        boolean liked = false;

        if (currentUserId != null) {
            User currentUser = userRepository.findById(currentUserId).orElse(null);
            if (currentUser != null) {
                liked = postLikeRepository.existsByUserAndPost(currentUser, post);
            }
        }

        return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
    }

    public Page<PostDto.PostResponse> getFeed(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 팔로우한 사용자들의 게시물
        List<Long> followingIds = followRepository.findByFollower(user).stream()
                .map(follow -> follow.getFollowing().getId())
                .collect(Collectors.toList());
        followingIds.add(userId); // 자신의 게시물도 포함

        Page<Post> posts = postRepository.findByAuthorIds(followingIds, pageable);
        return posts.map(post -> {
            long likeCount = postLikeRepository.countByPost(post);
            long commentCount = commentRepository.countByPost(post);
            boolean liked = postLikeRepository.existsByUserAndPost(user, post);
            return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
        });
    }

    public Page<PostDto.PostResponse> getPostsByHobby(Long hobbyId, Long currentUserId, Pageable pageable) {
        Hobby hobby = hobbyRepository.findById(hobbyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 취미입니다."));

        Page<Post> posts = postRepository.findByHobby(hobby, pageable);
        User currentUser = currentUserId != null ? userRepository.findById(currentUserId).orElse(null) : null;

        return posts.map(post -> {
            long likeCount = postLikeRepository.countByPost(post);
            long commentCount = commentRepository.countByPost(post);
            boolean liked = currentUser != null && postLikeRepository.existsByUserAndPost(currentUser, post);
            return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
        });
    }

    public Page<PostDto.PostResponse> getRecommendedPosts(Long userId, Pageable pageable) {
        List<Long> hobbyIds = userHobbyRepository.findHobbyIdsByUserId(userId);
        User user = userRepository.findById(userId).orElse(null);

        if (hobbyIds.isEmpty()) {
            // 취미가 없으면 인기 게시물 반환
            return getPopularPosts(userId, pageable);
        }

        Page<Post> posts = postRepository.findByHobbyIds(hobbyIds, pageable);
        return posts.map(post -> {
            long likeCount = postLikeRepository.countByPost(post);
            long commentCount = commentRepository.countByPost(post);
            boolean liked = user != null && postLikeRepository.existsByUserAndPost(user, post);
            return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
        });
    }

    public Page<PostDto.PostResponse> getPopularPosts(Long currentUserId, Pageable pageable) {
        Page<Post> posts = postRepository.findPopularPosts(pageable);
        User currentUser = currentUserId != null ? userRepository.findById(currentUserId).orElse(null) : null;

        return posts.map(post -> {
            long likeCount = postLikeRepository.countByPost(post);
            long commentCount = commentRepository.countByPost(post);
            boolean liked = currentUser != null && postLikeRepository.existsByUserAndPost(currentUser, post);
            return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
        });
    }

    @Transactional
    public PostDto.PostResponse updatePost(Long postId, Long userId, PostDto.UpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getImageUrl() != null) {
            post.setImageUrl(request.getImageUrl());
        }

        long likeCount = postLikeRepository.countByPost(post);
        long commentCount = commentRepository.countByPost(post);
        User user = userRepository.findById(userId).orElse(null);
        boolean liked = user != null && postLikeRepository.existsByUserAndPost(user, post);

        return PostDto.PostResponse.from(post, likeCount, commentCount, liked);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("이미 좋아요한 게시물입니다.");
        }

        PostLike like = PostLike.builder()
                .user(user)
                .post(post)
                .build();

        postLikeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        postLikeRepository.deleteByUserAndPost(user, post);
    }
}
