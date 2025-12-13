package com.example.bookmark.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 식별용 (카카오 이메일)
    @Column(unique = true, length = 255)
    private String email;

    // 🔔 실제 리마인더 수신 이메일
    @Column(length = 255)
    private String notificationEmail;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(length = 100)
    private String name;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(length = 20)
    private String provider;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
