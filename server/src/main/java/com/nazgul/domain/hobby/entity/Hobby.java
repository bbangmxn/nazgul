package com.nazgul.domain.hobby.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hobbies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String icon;

    @Column(nullable = false)
    private String category;  // 예: SPORTS, ARTS, MUSIC, GAMES, OUTDOOR, TECH, etc.
}
