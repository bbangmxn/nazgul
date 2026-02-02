package com.nazgul.domain.hobby.service;

import com.nazgul.domain.hobby.dto.HobbyDto;
import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.hobby.entity.UserHobby;
import com.nazgul.domain.hobby.repository.HobbyRepository;
import com.nazgul.domain.hobby.repository.UserHobbyRepository;
import com.nazgul.domain.user.entity.User;
import com.nazgul.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HobbyService {

    private final HobbyRepository hobbyRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final UserRepository userRepository;

    public List<HobbyDto.HobbyResponse> getAllHobbies() {
        return hobbyRepository.findAll().stream()
                .map(hobby -> {
                    long userCount = userHobbyRepository.countByHobbyId(hobby.getId());
                    return HobbyDto.HobbyResponse.from(hobby, userCount);
                })
                .collect(Collectors.toList());
    }

    public List<HobbyDto.HobbyResponse> getHobbiesByCategory(String category) {
        return hobbyRepository.findByCategory(category).stream()
                .map(hobby -> {
                    long userCount = userHobbyRepository.countByHobbyId(hobby.getId());
                    return HobbyDto.HobbyResponse.from(hobby, userCount);
                })
                .collect(Collectors.toList());
    }

    public List<HobbyDto.HobbyResponse> searchHobbies(String keyword) {
        return hobbyRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(hobby -> {
                    long userCount = userHobbyRepository.countByHobbyId(hobby.getId());
                    return HobbyDto.HobbyResponse.from(hobby, userCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public HobbyDto.HobbyResponse createHobby(HobbyDto.CreateRequest request) {
        if (hobbyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 취미입니다.");
        }

        Hobby hobby = Hobby.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .category(request.getCategory())
                .build();

        Hobby savedHobby = hobbyRepository.save(hobby);
        return HobbyDto.HobbyResponse.from(savedHobby, 0);
    }

    public List<HobbyDto.UserHobbyResponse> getUserHobbies(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return userHobbyRepository.findByUser(user).stream()
                .map(HobbyDto.UserHobbyResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public HobbyDto.UserHobbyResponse addUserHobby(Long userId, HobbyDto.AddUserHobbyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Hobby hobby = hobbyRepository.findById(request.getHobbyId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 취미입니다."));

        if (userHobbyRepository.existsByUserAndHobby(user, hobby)) {
            throw new IllegalArgumentException("이미 추가된 취미입니다.");
        }

        UserHobby userHobby = UserHobby.builder()
                .user(user)
                .hobby(hobby)
                .skillLevel(request.getSkillLevel() != null ? request.getSkillLevel() : 1)
                .build();

        UserHobby savedUserHobby = userHobbyRepository.save(userHobby);
        return HobbyDto.UserHobbyResponse.from(savedUserHobby);
    }

    @Transactional
    public void removeUserHobby(Long userId, Long hobbyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Hobby hobby = hobbyRepository.findById(hobbyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 취미입니다."));

        userHobbyRepository.deleteByUserAndHobby(user, hobby);
    }

    @Transactional
    public HobbyDto.UserHobbyResponse updateSkillLevel(Long userId, Long hobbyId, Integer skillLevel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Hobby hobby = hobbyRepository.findById(hobbyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 취미입니다."));

        UserHobby userHobby = userHobbyRepository.findByUserAndHobby(user, hobby)
                .orElseThrow(() -> new IllegalArgumentException("해당 취미가 등록되어 있지 않습니다."));

        userHobby.setSkillLevel(skillLevel);
        return HobbyDto.UserHobbyResponse.from(userHobby);
    }
}
