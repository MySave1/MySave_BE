package com.example.bookmark.domain.bookmark;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오 로그인으로 받은 userId
    private Long userId;

    private String url;
    private String title;
    // 원본 텍스트
    @Column(columnDefinition = "TEXT")
    private String content;
    // 요약 텍스트 (나중에 AI 붙일 때 사용)
    @Column(columnDefinition = "TEXT")
    private String summary;
    // 메모
    @Column(columnDefinition = "TEXT")
    private String memo;

    // 🔔 리마인더 시간
    private OffsetDateTime reminderAt;

    // ✅ 발송 여부 (중복 발송 방지)
    private boolean reminderSent = false;

    // 생성 시각
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // 태그
    @ElementCollection
    @CollectionTable(
            name = "bookmark_tags",
            joinColumns = @JoinColumn(name = "bookmark_id")
    )
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
}
