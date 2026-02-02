package com.nazgul.domain.user.service;

import com.nazgul.domain.hobby.repository.UserHobbyRepository;
import com.nazgul.domain.user.dto.UserDto;
import com.nazgul.domain.user.entity.Follow;
import com.nazgul.domain.user.entity.User;
import com.nazgul.domain.user.repository.FollowRepository;
import com.nazgul.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final UserHobbyRepository userHobbyRepository;

    public UserDto.UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        long followingCount = followRepository.countByFollower(user);
        long followersCount = followRepository.countByFollowing(user);
        
        return UserDto.UserResponse.from(user, followingCount, followersCount);
    }

    @Transactional
    public UserDto.UserResponse updateUser(Long userId, UserDto.UpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }

        long followingCount = followRepository.countByFollower(user);
        long followersCount = followRepository.countByFollowing(user);

        return UserDto.UserResponse.from(user, followingCount, followersCount);
    }

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    public List<UserDto.UserSummary> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return followRepository.findByFollowing(user).stream()
                .map(follow -> UserDto.UserSummary.from(follow.getFollower()))
                .collect(Collectors.toList());
    }

    public List<UserDto.UserSummary> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return followRepository.findByFollower(user).stream()
                .map(follow -> UserDto.UserSummary.from(follow.getFollowing()))
                .collect(Collectors.toList());
    }

    // 취미 기반 사용자 추천
    public List<UserDto.UserSummary> getRecommendedUsers(Long userId) {
        List<Long> hobbyIds = userHobbyRepository.findHobbyIdsByUserId(userId);
        
        if (hobbyIds.isEmpty()) {
            return List.of();
        }

        return userRepository.findByHobbiesIn(hobbyIds, userId).stream()
                .limit(10)
                .map(UserDto.UserSummary::from)
                .collect(Collectors.toList());
    }

    public List<UserDto.UserSummary> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword).stream()
                .map(UserDto.UserSummary::from)
                .collect(Collectors.toList());
    }
}
