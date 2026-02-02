package com.nazgul.domain.hobby.entity;

import com.nazgul.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_hobbies", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "hobby_id"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hobby_id", nullable = false)
    private Hobby hobby;

    @Column(nullable = false)
    @Builder.Default
    private Integer skillLevel = 1;  // 1: 입문, 2: 초급, 3: 중급, 4: 고급, 5: 전문가

    @CreationTimestamp
    private LocalDateTime createdAt;
}
