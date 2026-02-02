package com.nazgul.domain.hobby.dto;

import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.hobby.entity.UserHobby;
import lombok.*;

public class HobbyDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String name;
        private String description;
        private String icon;
        private String category;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddUserHobbyRequest {
        private Long hobbyId;
        private Integer skillLevel;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HobbyResponse {
        private Long id;
        private String name;
        private String description;
        private String icon;
        private String category;
        private long userCount;

        public static HobbyResponse from(Hobby hobby, long userCount) {
            return HobbyResponse.builder()
                    .id(hobby.getId())
                    .name(hobby.getName())
                    .description(hobby.getDescription())
                    .icon(hobby.getIcon())
                    .category(hobby.getCategory())
                    .userCount(userCount)
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserHobbyResponse {
        private Long id;
        private String name;
        private String icon;
        private String category;
        private Integer skillLevel;

        public static UserHobbyResponse from(UserHobby userHobby) {
            Hobby hobby = userHobby.getHobby();
            return UserHobbyResponse.builder()
                    .id(hobby.getId())
                    .name(hobby.getName())
                    .icon(hobby.getIcon())
                    .category(hobby.getCategory())
                    .skillLevel(userHobby.getSkillLevel())
                    .build();
        }
    }
}
