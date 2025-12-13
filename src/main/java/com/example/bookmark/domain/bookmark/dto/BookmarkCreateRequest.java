package com.example.bookmark.domain.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class BookmarkCreateRequest {

    private Long userId;
    private String url;
    private String title;

    // 본문 텍스트
    private String content;

    // 요약 텍스트 (나중에 페이지 다른 곳에 표시)
    private String summary;

    // 메모
    private String memo;

    // 태그들
    private List<String> tags;

    // 리마인더 시간
    private OffsetDateTime reminderAt;
}
